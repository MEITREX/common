package de.unistuttgart.iste.meitrex.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JsonSchemaGeneratorServiceTest {

    private JsonSchemaGeneratorService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = new JsonSchemaGeneratorService();
    }

    @Test
    void testGenerateSchemaForSimpleDto() {
        String jsonSchema = service.getJsonSchema(TestDto.class);

        assertThat(jsonSchema, notNullValue());

        JsonNode rootNode = assertDoesNotThrow(() -> objectMapper.readTree(jsonSchema),
                "Output should be valid JSON");

        assertThat(rootNode.get("type").asText(), is("object"));
        JsonNode properties = rootNode.get("properties");
        assertThat(properties, notNullValue());

        assertThat(properties.has("name"), is(true));
        assertThat(properties.get("name").get("type").asText(), is("string"));

        assertThat(properties.has("age"), is(true));
        assertThat(properties.get("age").get("type").asText(), is("integer"));
    }

    @Test
    void testSchemaCaching() {
        String firstCall = service.getJsonSchema(TestDto.class);

        String secondCall = service.getJsonSchema(TestDto.class);

        assertThat("Second call should return cached instance", firstCall, sameInstance(secondCall));
    }

    @Test
    void testIdFieldIsRemoved() {
        String jsonSchema = service.getJsonSchema(TestDto.class);

        assertThat(jsonSchema, not(containsString("\"id\":")));
    }

    record TestDto(String name, int age, boolean isActive) {}
}