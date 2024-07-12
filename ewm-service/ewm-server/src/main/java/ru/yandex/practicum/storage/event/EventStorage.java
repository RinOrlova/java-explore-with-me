package ru.yandex.practicum.storage.event;

import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.enums.Sort;

import java.util.Collection;
import java.util.List;

public interface EventStorage {

    Collection<EventShort> getEvents(String text,
                                     List<Integer> categoryId,
                                     Boolean paid,
                                     String rangeStart,
                                     String rangeEnd,
                                     boolean onlyAvailable,
                                     Sort sort,
                                     Integer from,
                                     Integer size);

    EventShort getEventShortById(Long id);

    EventFull getEventFullById(Long id);

    EventFull getEventByCreatorAndId(Long userId, Long eventId);
}
