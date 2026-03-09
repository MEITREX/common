package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.iste.meitrex.common.config.OllamaConfig;
import de.unistuttgart.iste.meitrex.common.service.JsonSchemaGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OllamaClientTest {

    @Mock
    private OllamaConfig config;
    @Mock
    private JsonSchemaGeneratorService jsonSchemaService;
    @Mock
    private HttpClient httpClient;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    private OllamaClient ollamaClient;

    @BeforeEach
    void setUp() {
        OllamaClient realClient = new OllamaClient(config, jsonSchemaService, jsonMapper, httpClient);
        ollamaClient = spy(realClient);
    }

    @Test
    void testFillTemplateSuccess() {
        String template = "Hello {{name}}, welcome to {{place}}!";
        Map<String, String> args = Map.of(
                "name", "MEITREX",
                "place", "Stuttgart"
        );

        String result = ollamaClient.fillTemplate(template, args);

        assertThat(result, is("Hello MEITREX, welcome to Stuttgart!"));
    }

    @Test
    void testFillTemplateThrowsOnUnknownArgument() {
        String template = "Hello {{name}}!";
        Map<String, String> args = Map.of("place", "Stuttgart");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                ollamaClient.fillTemplate(template, args)
        );
        assertThat(exception.getMessage(), containsString("No such argument in this prompt: {{place}}"));
    }

    @Test
    void testStartQuerySuccess() throws Exception {
        String templateFileName = "test_prompt.md";
        String templateContent = "Calculate 1+1";
        Map<String, String> args = Map.of();

        String mockSchema = "{\"type\":\"object\", \"properties\":{\"result\":{\"type\":\"integer\"}}}";

        String liteLlmJsonResponse = """
            {
              "choices": [
                {
                  "message": {
                    "content": "{\\"result\\": 2}"
                  }
                }
              ]
            }
        """;

        doReturn(templateContent).when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("mixtral:8x22b");
        when(config.getUrl()).thenReturn("http://localhost:4000");
        when(config.getEndpoint()).thenReturn("v1/chat/completions");
        when(config.getApiKey()).thenReturn("sk-test-api-key");

        when(jsonSchemaService.getJsonSchema(TestResponseDto.class)).thenReturn(mockSchema);

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(liteLlmJsonResponse);

        when(httpClient.send(
            any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(mockHttpResponse);

        TestResponseDto result = ollamaClient.startQuery(
            TestResponseDto.class,
            templateFileName,
            args,
            new TestResponseDto(0)
        );

        assertThat(result, notNullValue());
        assertThat(result.result(), is(2));

        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        verify(httpClient).send(requestCaptor.capture(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());

        HttpRequest sentRequest = requestCaptor.getValue();
        assertThat(sentRequest.uri().toString(), is("http://localhost:4000/v1/chat/completions"));

        var authHeader = sentRequest.headers().map().get("Authorization");
        assertThat(authHeader, is(notNullValue()));
        assertThat(authHeader.get(0), is("Bearer sk-test-api-key"));
    }

    @Test
    void testStartQueryHandlesNetworkError() throws Exception {
        String templateFileName = "error_test.md";
        doReturn("some prompt").when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("mixtral:8x22b");
        when(config.getUrl()).thenReturn("http://localhost:4000");
        when(config.getEndpoint()).thenReturn("v1/chat/completions");

        when(jsonSchemaService.getJsonSchema(any())).thenReturn("{\"properties\":{}}");

        when(httpClient.send(
            any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenThrow(new IOException("Network down"));

        TestResponseDto fallback = new TestResponseDto(-1);

        TestResponseDto result = ollamaClient.startQuery(
                TestResponseDto.class, templateFileName, Map.of(), fallback
        );

        assertThat(result, sameInstance(fallback));
    }

    @Test
    void testStartQueryHandlesLiteLlmError() throws Exception {
        String templateFileName = "api_error.md";
        doReturn("some prompt").when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("mixtral:8x22b");
        when(config.getUrl()).thenReturn("http://localhost:4000");
        when(config.getEndpoint()).thenReturn("v1/chat/completions");

        when(jsonSchemaService.getJsonSchema(any())).thenReturn("{\"properties\":{}}");

        String errorJson = "{\"error\": {\"message\": \"Authentication Error\", \"type\": \"auth_error\", \"code\": 401}}";

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(errorJson);

        when(httpClient.send(
            any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(mockHttpResponse);

        TestResponseDto fallback = new TestResponseDto(-1);

        TestResponseDto result = ollamaClient.startQuery(
                TestResponseDto.class, templateFileName, Map.of(), fallback
        );

        assertThat(result, sameInstance(fallback));
    }

    @Test
    void testStartQueryHandlesInvalidContentJson() throws Exception {
        String templateFileName = "invalid_json.md";
        doReturn("some prompt").when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("mixtral:8x22b");
        when(config.getUrl()).thenReturn("http://localhost:4000");
        when(config.getEndpoint()).thenReturn("v1/chat/completions");

        when(jsonSchemaService.getJsonSchema(any())).thenReturn("{\"properties\":{}}");

        // The outer envelope is valid, but the LLM hallucinated broken JSON inside the content string
        String brokenContentJsonResponse = """
            {
              "choices": [
                {
                  "message": {
                    "content": "This is not valid JSON text"
                  }
                }
              ]
            }
        """;

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(brokenContentJsonResponse);

        when(httpClient.send(
            any(HttpRequest.class),
            ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()
        )).thenReturn(mockHttpResponse);

        TestResponseDto fallback = new TestResponseDto(-1);

        TestResponseDto result = ollamaClient.startQuery(
            TestResponseDto.class, templateFileName, Map.of(), fallback
        );

        assertThat(result, sameInstance(fallback));
    }

    record TestResponseDto(int result) {}
}