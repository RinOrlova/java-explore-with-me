package ru.yandex.practicum.dto.comments;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
@Jacksonized
public class UpdateCommentRequest {

    @NotBlank
    @Size(max = 7000)
    String text;

}
