package ru.yandex.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.error.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownException(RuntimeException exc) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Server error")
                .message("Error occurred on server side.")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
