package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.common.serialization.LocalDateTimeSerializer;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;

import java.time.LocalDateTime;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class EventShort {

    private String annotation;
    private Category category;
    private long confirmedRequests;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime eventDate;
    private Long id;
    private UserFull initiator;
    private boolean paid;
    private String title;
    private long views;
}
