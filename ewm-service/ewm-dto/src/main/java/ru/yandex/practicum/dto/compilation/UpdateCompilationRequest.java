package ru.yandex.practicum.dto.compilation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.util.Collection;

@Value
@Builder
@Jacksonized
public class UpdateCompilationRequest {
    @Nullable
    Collection<Long> events;
    @Builder.Default
    Boolean pinned = false;
    @Nullable
    @Size(max = 50) String title;

}
