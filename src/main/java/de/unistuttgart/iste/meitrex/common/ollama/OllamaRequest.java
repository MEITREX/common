package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public record OllamaRequest(
        @JsonProperty("model") String model,
        @JsonProperty("messages") List<Message> messages,
        @JsonProperty("response_format") ResponseFormat responseFormat,
        @JsonProperty("stream") boolean stream,
        @JsonProperty("temperature") double temperature
) {
    public record Message(@JsonProperty("role") String role, @JsonProperty("content") String content) {}

    public record ResponseFormat(@JsonProperty("type") String type, @JsonProperty("json_schema") JsonSchema jsonSchema) {}

    public record JsonSchema(@JsonProperty("name") String name, @JsonProperty("strict") boolean strict, @JsonProperty("schema") Map<String, Object> schema) {}

    // Constructor for backwards compatibility
    public OllamaRequest(String model, String prompt, boolean stream, Map<String, Object> schemaMap) {
        this(
            model,
            List.of(new Message("user", prompt)),
            new ResponseFormat("json_schema", new JsonSchema("meitrex_schema", true, schemaMap)),
            stream,
            0.0
        );
    }
}
