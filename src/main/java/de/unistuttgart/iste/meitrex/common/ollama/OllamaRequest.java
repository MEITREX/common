package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record OllamaRequest(
        @JsonProperty("model") String model,
        @JsonProperty("prompt") String prompt,
        @JsonProperty("stream") boolean stream,
        @JsonProperty("format") Map<String, Object> format
) {}

