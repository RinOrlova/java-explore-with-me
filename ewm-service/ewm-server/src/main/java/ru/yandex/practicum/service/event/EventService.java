package ru.yandex.practicum.service.event;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventRequestAdmin;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;

import java.util.Collection;
import java.util.List;

public interface EventService {

    Collection<EventShort> getEventsPublic(PublicSearch publicSearch);

    EventShort getEventById(Long id);

    EventFull getEventByCreatorAndId(@NonNull Long userId,
                                     @NonNull Long eventId);

    EventFull updateEvent(@NonNull Long userId,
                          @NonNull Long eventId,
                          @NonNull EventRequest eventRequest);

    EventFull updateEventAdmin(Long eventId, EventRequestAdmin eventRequestAdmin);

    List<EventShort> getEventsByCreator(@NonNull Long userId,
                                        int from,
                                        int size);

    EventFull addEvent(@NonNull Long userId,
                       @NonNull EventRequest eventRequest);

    Collection<EventFull> searchEvents(@NonNull AdminSearch adminSearch);
}
