package ru.yandex.practicum.dto.comments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.dto.user.UserFull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentResponse {
    @NotBlank @Size(max = 7000) String text;
    @NotNull @Positive UserFull user;
    @NotNull @Positive Long eventId;
    @NotNull LocalDateTime created;
    @Nullable
    LocalDateTime edited;
    @NotNull Long id;

}
