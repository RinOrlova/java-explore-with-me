package ru.yandex.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.error.ErrorResponse;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException exc) {
        log.error("Operation failed with an exception: {}", exc.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.name())
                .reason("For the requested operation the conditions are not met.")
                .message(exc.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler({InvalidDateRequestedException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidRequetException(RuntimeException exc) {
        log.error("Operation failed with an exception: {}", exc.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Request validation failed.")
                .message(exc.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(EntityNotFoundException exc) {
        log.warn("Requested entity not found: {}", exc.getMessage());
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("The required object was not found.")
                .message(exc.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException exc) {
        log.error("Operation failed due to conflict: {}", exc.getMessage(), exc);
        return ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Can't update entity.")
                .message(exc.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnknownException(RuntimeException exc) {
        log.error("Operation failed with an RuntimeException: ", exc);
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Server error")
                .message("Error occurred on server side.")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
