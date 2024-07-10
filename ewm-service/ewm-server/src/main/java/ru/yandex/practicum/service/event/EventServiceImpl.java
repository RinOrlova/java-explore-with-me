package ru.yandex.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.storage.event.EventStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    @Override
    public Collection<EventShort> getEvents(String text,
                                            @Nullable List<Integer> categoryId,
                                            @Nullable Boolean paid,
                                            @Nullable String rangeStart,
                                            @Nullable String rangeEnd,
                                            boolean onlyAvailable,
                                            @Nullable Sort sort,
                                            Integer from,
                                            Integer size) {
        return eventStorage.getEvents(text, categoryId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @Override
    public EventShort getEventById(Long id) {
        return eventStorage.getEventShortById(id);
    }
}
