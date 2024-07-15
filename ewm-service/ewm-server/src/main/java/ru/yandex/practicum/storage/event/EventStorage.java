package ru.yandex.practicum.storage.event;

import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;

import java.util.Collection;
import java.util.List;

public interface EventStorage {


    EventShort getEventShortById(Long id);

    EventFull getEventFullById(Long id);

    EventFull getEventByCreatorAndId(Long userId, Long eventId);

    EventFull updateEvent(EventRequest eventRequest, Long userId);

    EventFull addEvent(EventRequest eventRequest, Long userId);

    boolean isUserOwnerOfEvent(Long userId, Long eventId);

    boolean anyFreePlacesLeft(Long eventId);

    Collection<EventShort> searchEventsPublic(PublicSearch publicSearch);

    Collection<EventFull> searchEvents(AdminSearch adminSearch);

    EventFull updateEventAdmin(EventFull updatedEvent);

    List<EventShort> getEventByCreator(Long userId, int from, int size);
}
