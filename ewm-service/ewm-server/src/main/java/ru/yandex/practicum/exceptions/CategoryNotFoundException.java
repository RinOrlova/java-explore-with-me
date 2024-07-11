package ru.yandex.practicum.exceptions;

public class CategoryNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "Category by id=%s not found";

    public CategoryNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }


}
