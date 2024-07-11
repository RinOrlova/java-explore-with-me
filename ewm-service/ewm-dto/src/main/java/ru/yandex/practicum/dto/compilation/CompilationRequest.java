package ru.yandex.practicum.dto.compilation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Value
@Builder
@Jacksonized
public class CompilationRequest {

    Collection<Long> events;
    @Builder.Default
    Boolean pinned = false;
    @NotBlank
    String title;

}
