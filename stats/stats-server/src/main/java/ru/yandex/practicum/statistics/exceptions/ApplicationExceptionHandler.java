package ru.yandex.practicum.statistics.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.common.dto.ErrorResponse;
@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDateRequestedException(InvalidDateRequestedException exc) {
        log.error("Invalid date requested: ", exc);
        return new ErrorResponse("Invalid data in request.", exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidDateFormatRequestedException(InvalidDateFormatRequestedException exc) {
        log.error("Invalid date format requested: ", exc);
        return new ErrorResponse("Invalid data in request.", exc.getMessage());
    }

}
