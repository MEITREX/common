package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.Map;

public record OllamaRequest(@JsonProperty("model") String model, @JsonProperty("prompt") String prompt,
                            @JsonProperty("stream") boolean stream,
                            @JsonProperty("format") Map<String, Object> format) {

    public OllamaRequest(String model, String prompt, boolean stream, Map<String, Object> format) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
        this.format = format;
    }
}

