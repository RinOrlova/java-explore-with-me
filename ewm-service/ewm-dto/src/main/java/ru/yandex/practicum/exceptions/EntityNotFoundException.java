package ru.yandex.practicum.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Entity %s by id=%s not found";

    public EntityNotFoundException(Long id, Class<?> classType) {
        super(String.format(DEFAULT_MESSAGE, classType.getName(), id));
    }
}
