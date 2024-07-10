package service.eventService;

import dto.event.EventShort;
import org.springframework.lang.Nullable;
import utils.Sort;

import java.util.ArrayList;
import java.util.Collection;

public interface EventService {

    Collection<EventShort> getEvents(String text,
                                     @Nullable ArrayList<Integer> categoryId,
                                     @Nullable Boolean paid,
                                     @Nullable String rangeStart,
                                     @Nullable String rangeEnd,
                                     @Nullable String onlyAvailable,
                                     @Nullable Sort sort,
                                     @Nullable Integer from,
                                     @Nullable Integer size);

    EventShort getEventById(Long id);
}
