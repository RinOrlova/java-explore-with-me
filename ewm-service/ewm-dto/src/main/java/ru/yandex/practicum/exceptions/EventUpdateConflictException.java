package ru.yandex.practicum.exceptions;

public class EventUpdateConflictException extends ConflictException {

    public EventUpdateConflictException(String mesage) {
        super(mesage);
    }
}
