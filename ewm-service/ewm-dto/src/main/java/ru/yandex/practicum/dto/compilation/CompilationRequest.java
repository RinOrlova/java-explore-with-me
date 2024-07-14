package ru.yandex.practicum.dto.compilation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Value
@Builder
@Jacksonized
public class CompilationRequest {
    @Nullable
    Collection<Long> events;
    @Builder.Default
    Boolean pinned = false;
    @NotNull
    @NotBlank
    @Size(max = 50)
    String title;

}
