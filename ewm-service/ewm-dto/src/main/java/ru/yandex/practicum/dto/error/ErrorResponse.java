package ru.yandex.practicum.dto.error;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ErrorResponse {
    String status;
    String reason;
    String message;
    LocalDateTime timestamp;
}
