package ru.yandex.practicum.common.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.practicum.common.utils.StatisticsConstants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateTimeParam = jsonParser.getText();
        try {
            return LocalDateTime.parse(dateTimeParam, DEFAULT_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(dateTimeParam, StatisticsConstants.DATE_TIME_FORMATTER);
        }
    }
}
