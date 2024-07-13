package ru.yandex.practicum.dto.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.dto.event.EventStatus;

import java.util.Collections;
import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdminSearch extends AbstractSearch {
    @Builder.Default
    List<Long> userIds = Collections.emptyList();
    @Builder.Default
    List<EventStatus> states = Collections.emptyList();
}
