package ru.yandex.practicum.client;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.utils.StatisticsConstants;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeEncoder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(StatisticsConstants.DATE_FORMAT);

    public String encodeDateTime(LocalDateTime dateTime) {
        String formattedDateTime = dateTime.format(DATE_FORMATTER);
        return URLEncoder.encode(formattedDateTime, StandardCharsets.UTF_8);
    }

}
