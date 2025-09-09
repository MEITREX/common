package de.unistuttgart.iste.meitrex.common.dapr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dapr.serializer.DefaultObjectSerializer;

import java.io.IOException;

public class CustomDaprObjectSerializer extends DefaultObjectSerializer {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(Object state) throws IOException {
        return mapper.writeValueAsBytes(state);
    }

    @Override
    public <T> T deserialize(byte[] content, Class<T> clazz) throws IOException {
        return mapper.readValue(content, clazz);
    }
}