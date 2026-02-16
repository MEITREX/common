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
import java.util.Optional;

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
    void testStartQuerySuccess() throws IOException, InterruptedException {
        String templateFileName = "test_prompt.md";
        String templateContent = "Calculate 1+1";
        Map<String, String> args = Map.of();
        String mockSchema = "{\"type\":\"object\"}";
        String ollamaJsonResponse = "{\"response\": \"{\\\"result\\\": 2}\", \"done\": true}";

        doReturn(templateContent).when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("llama3");
        when(config.getUrl()).thenReturn("http://localhost:11434");
        when(config.getEndpoint()).thenReturn("api/generate");

        when(jsonSchemaService.getJsonSchema(TestResponseDto.class)).thenReturn(mockSchema);

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(ollamaJsonResponse);

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
        assertThat(sentRequest.uri().toString(), is("http://localhost:11434/api/generate"));
    }

    @Test
    void testStartQueryHandlesNetworkError() throws IOException, InterruptedException {
        String templateFileName = "error_test.md";
        doReturn("some prompt").when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("llama3");
        when(config.getUrl()).thenReturn("http://localhost:11434");
        when(config.getEndpoint()).thenReturn("api/generate");

        when(jsonSchemaService.getJsonSchema(any())).thenReturn("{}");

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
    void testStartQueryHandlesOllamaError() throws IOException, InterruptedException {
        String templateFileName = "ollama_error.md";
        doReturn("some prompt").when(ollamaClient).getTemplate(templateFileName);

        when(config.getModel()).thenReturn("llama3");
        when(config.getUrl()).thenReturn("http://localhost:11434");
        when(config.getEndpoint()).thenReturn("api/generate");

        when(jsonSchemaService.getJsonSchema(any())).thenReturn("{}");

        String errorJson = "{\"error\": \"Model not found\"}";
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
    void testParseResponseValidJson() {
        OllamaResponse response = new OllamaResponse();
        response.setResponse("{\"result\": 42}");

        Optional<TestResponseDto> result = ollamaClient.parseResponse(response, TestResponseDto.class);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().result(), is(42));
    }

    @Test
    void testParseResponseInvalidJson() {
        OllamaResponse response = new OllamaResponse();
        response.setResponse("This is not JSON");
        Optional<TestResponseDto> result = ollamaClient.parseResponse(response, TestResponseDto.class);
        assertThat(result.isEmpty(), is(true));
    }

    record TestResponseDto(int result) {}
}