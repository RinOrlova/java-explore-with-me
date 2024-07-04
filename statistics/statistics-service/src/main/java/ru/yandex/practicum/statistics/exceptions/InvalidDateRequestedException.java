package ru.yandex.practicum.statistics.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidDateRequestedException extends RuntimeException{

    private static final String EXCEPTION_MESSAGE = "Start=%s must be before or equal end=%s";

    public InvalidDateRequestedException(LocalDateTime start,
                                         LocalDateTime end){
        super(String.format(EXCEPTION_MESSAGE, start, end));
    }
}
