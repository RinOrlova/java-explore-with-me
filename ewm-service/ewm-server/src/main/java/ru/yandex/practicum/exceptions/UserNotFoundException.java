package ru.yandex.practicum.exceptions;

public class UserNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "User by id=%s not found";

    public UserNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }


}
