package ru.yandex.practicum.statistics.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.statistics.exceptions.InvalidDateFormatRequestedException;
import ru.yandex.practicum.statistics.utils.TestConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataTimeParamsDecoderTest {

    private final DataTimeParamsDecoder dataTimeParamsDecoder = new DataTimeParamsDecoder();

    @Test
    void positive_regularEncodedString() {
        String formattedDateTime = TestConstants.defaultTimestamp.format(StatisticsConstants.DATE_TIME_FORMATTER);
        LocalDateTime decodedValue = dataTimeParamsDecoder.getLocalDateTimeParamOfEncodedValue(formattedDateTime);
        assertEquals(TestConstants.defaultTimestamp, decodedValue);
    }

    @Test
    void negative_notLocalDateTimeFormat_exceptionExpected() {
        String formattedDateTime = TestConstants.defaultTimestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertThrows(InvalidDateFormatRequestedException.class, () -> dataTimeParamsDecoder.getLocalDateTimeParamOfEncodedValue(formattedDateTime));
    }

    @Test
    void negative_emptyInput_exceptionExpected() {
        String formattedDateTime = "";
        assertThrows(InvalidDateFormatRequestedException.class, () -> dataTimeParamsDecoder.getLocalDateTimeParamOfEncodedValue(formattedDateTime));
    }

    @Test
    void negative_nullInput_exceptionExpected() {
        String formattedDateTime = null;
        assertThrows(InvalidDateFormatRequestedException.class, () -> dataTimeParamsDecoder.getLocalDateTimeParamOfEncodedValue(formattedDateTime));
    }

    @Test
    void negative_invalidInputForDecoder_exceptionExpected() {
        String formattedDateTime = "%%%";
        assertThrows(InvalidDateFormatRequestedException.class, () -> dataTimeParamsDecoder.getLocalDateTimeParamOfEncodedValue(formattedDateTime));
    }
}