package ru.yandex.practicum.service.event;

import ru.yandex.practicum.dto.event.EventShort;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.enums.Sort;

import java.util.List;
import java.util.Collection;

public interface EventService {

    Collection<EventShort> getEvents(String text,
                                     @Nullable List<Integer> categoryId,
                                     @Nullable Boolean paid,
                                     @Nullable String rangeStart,
                                     @Nullable String rangeEnd,
                                     boolean onlyAvailable,
                                     @Nullable Sort sort,
                                     Integer from,
                                     Integer size);

    EventShort getEventById(Long id);
}
