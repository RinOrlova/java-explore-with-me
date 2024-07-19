package ru.yandex.practicum.exceptions;

public class CategoryNameConstraintException extends ConflictException {

    private static final String DEFAULT_MESSAGE = "Category with equal name already exists";

    public CategoryNameConstraintException() {
        super(DEFAULT_MESSAGE);
    }
}
