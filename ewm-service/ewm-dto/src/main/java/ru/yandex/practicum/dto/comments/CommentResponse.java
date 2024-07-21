package ru.yandex.practicum.dto.comments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentResponse {
    String text;
    Long userId;
    Long eventId;
    LocalDateTime created;
    LocalDateTime edited;
    Long id;

}
