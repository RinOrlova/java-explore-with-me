package ru.yandex.practicum.controller.deserialization;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.utils.StatisticsConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime convert(@NonNull String source) {
        try {
            return LocalDateTime.parse(source, DEFAULT_FORMATTER);
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(source, StatisticsConstants.DATE_TIME_FORMATTER);
        }
    }
}

