package ru.yandex.practicum.exceptions;

public class ForbiddenException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Action forbidden";

    public ForbiddenException() {
        super(DEFAULT_MESSAGE);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}

