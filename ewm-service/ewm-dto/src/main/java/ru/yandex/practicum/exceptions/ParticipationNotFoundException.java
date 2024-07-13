package ru.yandex.practicum.exceptions;

public class ParticipationNotFoundException extends NotFoundException {
    private static final String DEFAULT_MESSAGE = "No participation request found by id=%s";

    public ParticipationNotFoundException(Long requestId) {
        super(String.format(DEFAULT_MESSAGE, requestId));
    }
}
