package ru.yandex.practicum.dto.compilation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;
import javax.validation.constraints.Size;
import java.util.Set;

@Value
@Builder
@Jacksonized
public class UpdateCompilationRequest {
    @Nullable
    Set<Long> events;
    @Builder.Default
    Boolean pinned = false;
    @Nullable
    @Size(max = 50) String title;

}
