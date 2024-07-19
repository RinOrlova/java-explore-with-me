package ru.yandex.practicum.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;
import ru.yandex.practicum.common.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class HitRequest {

    private String app;
    private String uri;
    private String ip;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
}
