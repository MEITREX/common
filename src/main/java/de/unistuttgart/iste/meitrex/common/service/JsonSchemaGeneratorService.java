package de.unistuttgart.iste.meitrex.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for generating and caching JSON schemas from Java DTO classes.
 * This is used to inform LLMs about the expected structured output format.
 */
@Service
@Slf4j
public class JsonSchemaGeneratorService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Class<?>, String> schemaCache = new ConcurrentHashMap<>();

    /**
     * Generates a JSON schema for the given class.
     * Results are cached to improve performance.
     *
     * @param dtoClass The class for which to generate the schema.
     * @return A JSON string representing the schema.
     * @throws RuntimeException if the JSON transformation fails.
     */
    public String getJsonSchema(final Class<?> dtoClass) {
        return schemaCache.computeIfAbsent(dtoClass, key -> {
            try {
                final JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(objectMapper);
                final JsonSchema schema = schemaGen.generateSchema(key);

                // Removing the ID field often makes the schema cleaner for LLM context windows
                schema.setId(null);

                log.info("Generated new JSON schema for class: {}", key.getSimpleName());
                return objectMapper.writeValueAsString(schema);
            } catch (JsonProcessingException e) {
                final StringBuilder errorBuilder = new StringBuilder();
                errorBuilder.append("Failed to generate JSON schema for class: ")
                        .append(key.getName());

                log.error(errorBuilder.toString(), e);
                throw new RuntimeException(errorBuilder.toString(), e);
            }
        });
    }
}
