package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.yandex.practicum.common.deserialize.LocalDateTimeDeserializer;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFull {

    private String annotation;
    private Category category;
    private Long id;
    private String name;
    private Long confirmedRequests;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    private User initiator;
    private Long userId;
    private String userName;
    Boolean paid;
    private String title;
    private Long views;



}
