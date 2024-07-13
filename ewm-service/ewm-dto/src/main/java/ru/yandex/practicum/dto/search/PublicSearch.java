package ru.yandex.practicum.dto.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.dto.search.enums.SortType;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PublicSearch extends AbstractSearch {

    @Nullable
    String text;
    @Nullable
    Boolean paid;
    @Builder.Default
    boolean onlyAvailable = false;
    @Nullable
    SortType sort;
}
