package ru.yandex.practicum.exceptions;

public class CompilationNotFoundException extends NotFoundException{

    private static final String DEFAULT_MESSAGE = "Compilation by id=%s not found";

    public CompilationNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }


}
