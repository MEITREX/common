package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OllamaResponse(
        @JsonProperty("choices") List<Choice> choices,
        @JsonProperty("error") Object error
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(@JsonProperty("message") Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(@JsonProperty("content") String content) {}

    // Convenience method to extract the actual text safely
    public String getContent() {
        if (choices != null && !choices.isEmpty() && choices.get(0).message() != null) {
            return choices.get(0).message().content();
        }
        return null;
    }
}