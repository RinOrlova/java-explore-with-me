package ru.yandex.practicum.dto.category;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CategoryTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void test_deserializeCategory() throws JacksonException {
        String categoryRequestJson = "{\"name\":\"UserName\"}";
        Category category = objectMapper.readValue(categoryRequestJson, Category.class);
        assertEquals("UserName", category.getName());
        assertNull(category.getId());
    }

    @Test
    void test_deserializeCategory_full() throws JacksonException {
        String categoryRequestJson = "{\"id\":\"1\",\"name\":\"UserName\"}";
        Category category = objectMapper.readValue(categoryRequestJson, Category.class);
        assertEquals("UserName", category.getName());
        assertEquals(1L, category.getId());
    }
}