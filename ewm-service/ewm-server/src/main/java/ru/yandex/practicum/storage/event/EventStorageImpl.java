package ru.yandex.practicum.storage.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.exceptions.NotFoundException;
import ru.yandex.practicum.mapper.EventMapper;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventStorageImpl implements EventStorage {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public Collection<EventShort> getEvents(String text,
                                            List<Integer> categoryId,
                                            Boolean paid,
                                            String rangeStart,
                                            String rangeEnd,
                                            boolean onlyAvailable,
                                            Sort sort,
                                            Integer from,
                                            Integer size) {
        return null;
    }

    @Override
    public EventShort getEventShortById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::mapToEventShort)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public EventFull getEventFullById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::mapToEventFull)
                .orElseThrow(NotFoundException::new);
    }
}
