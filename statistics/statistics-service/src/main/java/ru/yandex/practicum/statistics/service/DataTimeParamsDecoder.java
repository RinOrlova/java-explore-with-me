package ru.yandex.practicum.statistics.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.statistics.exceptions.InvalidDateFormatRequestedException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataTimeParamsDecoder {

    public LocalDateTime getLocalDateTimeParamOfEncodedValue(String param) {
        try {
            String encodedParam = encodeParam(param);
            return LocalDateTime.parse(encodedParam, DateTimeFormatter.ofPattern(StatisticsConstants.DATE_FORMAT));
        } catch (RuntimeException exception) {
            throw new InvalidDateFormatRequestedException(param);
        }
    }

    private String encodeParam(String param) {
        return URLDecoder.decode(param, StandardCharsets.UTF_8);
    }

}
