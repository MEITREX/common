package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.iste.meitrex.common.config.OllamaConfig;
import de.unistuttgart.iste.meitrex.common.service.JsonSchemaGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OllamaClient {

    private final OllamaConfig config;
    private final JsonSchemaGeneratorService jsonSchemaService;

    private final ObjectMapper jsonMapper;
    private final HttpClient client;

    /**
     * Loads a prompt template from the resources folder using try-with-resources.
     *
     * @param templateFileName the name of the template file
     * @return the content of the template file as a UTF-8 encoded string
     * @throws RuntimeException if the file is not found or cannot be read
     */
    public String getTemplate(final String templateFileName) {
        final String folder = config.getPromptFolder();
        final String sanitizedFolder = folder.startsWith("/") ? folder : "/" + folder;

        final String path = sanitizedFolder.endsWith("/")
                ? sanitizedFolder + templateFileName
                : sanitizedFolder + "/" + templateFileName;

        log.debug("Attempting to load template from path: {}", path);

        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Template file not found: " + templateFileName);
            }

            log.info("Reading template: {}", templateFileName);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines()
                    .collect(Collectors.joining("\n"))
                    .concat("\n");
            }
        } catch (IOException e) {
            log.error("Failed to read template file: {}", templateFileName, e);
            throw new RuntimeException("Failed to read template file: " + templateFileName, e);
        }
    }

    /**
     * Replaces placeholders in a given prompt template with the corresponding argument values.
     * <p>
     * Each placeholder must be wrapped in double curly braces, e.g., <code>{{argumentName}}</code>.
     * For every key provided in the Map, the method searches for its placeholder in the template
     * and replaces it with the associated argument value.
     * </p>
     *
     * @param promptTemplate the template string containing placeholders in the form <code>{{argumentName}}</code>
     * @param args a map where keys are argument names and values are their replacements
     * @return the template string with all placeholders replaced by their corresponding values
     * @throws IllegalArgumentException if the template does not contain a placeholder for any provided argument
     */
    public String fillTemplate(final String promptTemplate, final Map<String, String> args) {
        for (final String key : args.keySet()) {
            final String placeholder = "{{" + key + "}}";

            if (!promptTemplate.contains(placeholder)) {
                throw new IllegalArgumentException("No such argument in this prompt: " + placeholder);
            }
        }

        final Pattern pattern = Pattern.compile("\\{\\{.*?}}");
        final Matcher matcher = pattern.matcher(promptTemplate);
        final StringBuilder resultBuilder = new StringBuilder();

        while (matcher.find()) {
            final String fullPlaceholder = matcher.group();

            final String key = fullPlaceholder.substring(2, fullPlaceholder.length() - 2);
            final String value = args.get(key);

            if (value != null) {
                matcher.appendReplacement(resultBuilder, Matcher.quoteReplacement(value));
            }
        }
        matcher.appendTail(resultBuilder);

        return resultBuilder.toString();
    }

    /**
     * Executes a full LLM query cycle: loads a template from resources, fills it with arguments,
     * requests a structured response from Ollama, and parses the result.
     *
     * @param <ResponseType> the target type for the structured JSON response
     * @param responseType   the class of the target type to parse the response into
     * @param templateFileName   the name of the file in the prompt folder (e.g., "analysis_prompt.md")
     * @param argMap         a map of placeholder keys (without braces) and their replacement values
     * @param error          the fallback value to return if the query, parsing, or template loading fails
     * @param modelOverride  optional model name (e.g., "llama3.3:70b") to bypass the default config.
     * If null/blank, the default from {@link OllamaConfig} is used.
     * @return the parsed response object of type ResponseType, or the provided error fallback
     */
    public <ResponseType> ResponseType startQuery(
            final Class<ResponseType> responseType,
            final String templateFileName,
            final Map<String, String> argMap,
            final ResponseType error,
            @Nullable final String modelOverride) {
        try {
            final String promptTemplate = getTemplate(templateFileName);

            final String filledPrompt = fillTemplate(promptTemplate, argMap);

            final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
            final String jsonSchema = jsonSchemaService.getJsonSchema(responseType);
            final Map<String, Object> schemaObject = jsonMapper.readValue(jsonSchema, typeRef);

            // Determine which model to use: override or default config
            final String modelToUse = (modelOverride != null && !modelOverride.isBlank())
                    ? modelOverride
                    : this.config.getModel();

            log.info("Starting LLM query. Model: {}", modelToUse);

            OllamaRequest request = new OllamaRequest(
                    modelToUse,
                    filledPrompt,
                    false,
                    schemaObject
            );

            final OllamaResponse response = queryLLM(request);
            final Optional<ResponseType> parsedResponse = parseResponse(response, responseType);

            return parsedResponse.orElse(error);
        } catch (IOException | RuntimeException exception) {
            log.error("Error while starting query: {}", exception.getMessage(), exception);
            return error;
        } catch (InterruptedException e) {
            log.error("Query interrupted: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
            return error;
        }
    }

    /**
     * Overloaded helper method for backward compatibility.
     * Starts a query using the default model defined in the configuration.
     *
     * @param responseType the target class to parse the response into
     * @param templateFileName  the name of the file in the prompt folder (e.g., "analysis_prompt.md")
     * @param argMap       A map of placeholder keys and their replacement values.
     * @param error        the fallback value if parsing or the request fails
     * @return the parsed response or the fallback error value
     */
    public <ResponseType> ResponseType startQuery(
            final Class<ResponseType> responseType,
            final String templateFileName,
            final Map<String, String> argMap,
            final ResponseType error) {
        return startQuery(responseType, templateFileName, argMap, error, null);
    }

    /**
     * Sends the given request to the Ollama LLM endpoint and returns the raw response.
     *
     * @param request the request payload
     * @return the response from Ollama
     * @throws IOException if the request or response handling fails
     * @throws InterruptedException if the HTTP call is interrupted
     */
    private OllamaResponse queryLLM(OllamaRequest request) throws IOException, InterruptedException {
        final String json = jsonMapper.writeValueAsString(request);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(this.config.getUrl() + "/" + this.config.getEndpoint()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        OllamaResponse result = jsonMapper.readValue(response.body(), OllamaResponse.class);

        if (result.getError() != null) {
            throw new RuntimeException("Ollama returned error: " + result.getError());
        }

        return result;
    }

    /**
     * parse an ollama response to a specify type. It expects the response to be a valid json
     * If it fails to parse the response, it returns an empty optional
     *
     * @param ollamaResponse the response from the ollama server
     * @param responseType the type to parse the response to
     * @return an optional of the parsed response
     * @param <ResponseType> the type to cast to
     */
    public <ResponseType> Optional<ResponseType> parseResponse(OllamaResponse ollamaResponse, Class<ResponseType> responseType) {
        final String response = ollamaResponse.getResponse();
        if(responseType == null || response == null) {
            log.info("Response is null or empty: {}", response);
            return Optional.empty();
        }
        try {
            return Optional.of(jsonMapper.readValue(response, responseType));
        } catch (IOException e) {
            log.error("Failed to parse response: {}", response, e);
            return Optional.empty();
        }
    }

}
