package de.unistuttgart.iste.meitrex.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration()
@Getter
public class OllamaConfig {

    @Value("${ollama.url}")
    private String url;

    @Value("${ollama.model}")
    private String model;

    @Value("${ollama.endpoint}")
    private String endpoint;

    // Should be the name of the folder in which the prompts reside in the resource directory
    @Value("${ollama.promptFolder:prompt_templates}")
    private String promptFolder;
}
