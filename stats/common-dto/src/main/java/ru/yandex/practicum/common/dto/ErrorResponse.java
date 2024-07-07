package ru.yandex.practicum.common.dto;

import lombok.Value;

@Value
public class ErrorResponse {

    String error;
    String description;
}
