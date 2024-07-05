package ru.yandex.practicum.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private final LocalDateTime timestamp = LocalDateTime.now();
}
