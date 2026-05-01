package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OllamaResponse(
        @JsonProperty("choices") List<Choice> choices,
        @JsonProperty("error") OpenAiError error
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(@JsonProperty("message") Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(@JsonProperty("content") String content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OpenAiError(@JsonProperty("message") String message) {}

    @JsonIgnore
    public String getResponse() {
        if (choices != null && !choices.isEmpty() && choices.get(0).message() != null) {
            return choices.get(0).message().content();
        }
        return null;
    }

    @JsonIgnore
    public String getErrorMessage() {
        return error != null ? error.message() : null;
    }
}