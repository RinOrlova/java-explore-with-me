package service.eventService;

import dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import storage.eventStorage.EventStorage;
import utils.Sort;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    @Override
    public Collection<EventShort> getEvents(String text,
                                            @Nullable ArrayList<Integer> categoryId,
                                            @Nullable Boolean paid,
                                            @Nullable String rangeStart,
                                            @Nullable String rangeEnd,
                                            @Nullable String onlyAvailable,
                                            @Nullable Sort sort,
                                            @Nullable Integer from,
                                            @Nullable Integer size) {
        return eventStorage.getEvents(text, categoryId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @Override
    public EventShort getEventById(Long id) {
        return eventStorage.getEventById(id);
    }
}
