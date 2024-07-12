package ru.yandex.practicum.common.deserialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateTimeDeserializerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        OBJECT_MAPPER.registerModule(module);
    }

    @Test
    void testDeserializeWithFormat1() throws IOException {
        String json = "{\"timestamp\": \"2023-07-07 12:34:56\"}";
        LocalDateTime expected = LocalDateTime.of(2023, 7, 7, 12, 34, 56);

        HitRequest hitRequest = OBJECT_MAPPER.readValue(json, HitRequest.class);

        assertEquals(expected, hitRequest.getTimestamp());
    }

    @Test
    void testDeserializeWithFormat2() throws IOException {
        String json = "{\"timestamp\": \"2023-07-07T12:34:56\"}";
        LocalDateTime expected = LocalDateTime.of(2023, 7, 7, 12, 34, 56);

        HitRequest hitRequest = OBJECT_MAPPER.readValue(json, HitRequest.class);

        assertEquals(expected, hitRequest.getTimestamp());
    }

    @Test
    void testDeserializeWithInvalidFormat() {
        String json = "{\"timestamp\": \"2023-07-07\"}";

        assertThrows(IOException.class, () -> OBJECT_MAPPER.readValue(json, HitRequest.class));
    }

}