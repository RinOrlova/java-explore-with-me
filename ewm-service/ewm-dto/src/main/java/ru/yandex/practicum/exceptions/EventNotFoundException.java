package ru.yandex.practicum.exceptions;

public class EventNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "Event by id=%s not found";

    public EventNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }

}
