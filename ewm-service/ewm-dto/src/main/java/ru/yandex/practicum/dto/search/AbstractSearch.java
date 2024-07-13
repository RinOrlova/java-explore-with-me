package ru.yandex.practicum.dto.search;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Value
@NonFinal
@SuperBuilder
public abstract class AbstractSearch {
    @Builder.Default
    List<Long> categories = Collections.emptyList();
    @Nullable
    LocalDateTime rangeStart;
    @Nullable
    LocalDateTime rangeEnd;
    int from;
    int size;

}
