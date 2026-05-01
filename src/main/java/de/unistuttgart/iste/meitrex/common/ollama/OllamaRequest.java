package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record OllamaRequest(
        @JsonProperty("model") String model,
        @JsonProperty("messages") List<Message> messages,
        @JsonProperty("temperature") double temperature,
        @JsonProperty("response_format") Map<String, Object> responseFormat
) {
    public record Message(
            @JsonProperty("role") String role,
            @JsonProperty("content") String content
    ) {}
}

