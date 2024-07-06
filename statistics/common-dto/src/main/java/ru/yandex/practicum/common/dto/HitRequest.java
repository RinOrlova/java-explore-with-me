package ru.yandex.practicum.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitRequest {

    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
