package ru.yandex.practicum.statistics.exceptions;

import java.time.LocalDateTime;

public class InvalidDateFormatRequestedException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Invalid Date param=%s requested";

    public InvalidDateFormatRequestedException(String param){
        super(String.format(EXCEPTION_MESSAGE, param));
    }

}
