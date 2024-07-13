package ru.yandex.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.event.*;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;
import ru.yandex.practicum.exceptions.ForbiddenException;
import ru.yandex.practicum.storage.category.CategoryStorage;
import ru.yandex.practicum.storage.event.EventStorage;
import ru.yandex.practicum.storage.location.LocationStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final LocationStorage locationStorage;

    @Override
    public Collection<EventShort> getEventsPublic(PublicSearch publicSearch) {
        return eventStorage.searchEventsPublic(publicSearch);
    }

    @Override
    public EventShort getEventById(Long id) {
        return eventStorage.getEventShortById(id);
    }

    @Override
    public EventFull getEventByCreatorAndId(@NonNull Long userId, @NonNull Long eventId) {
        return eventStorage.getEventByCreatorAndId(userId, eventId);
    }

    @Override
    public EventFull updateEvent(@NonNull Long userId, @NonNull Long eventId, @NonNull EventRequest eventRequest) {
        EventFull eventFullById = eventStorage.getEventByCreatorAndId(userId, eventId);
        if (eventFullById.getState() != EventStatus.PUBLISHED) {
            return eventStorage.updateEvent(eventRequest, userId);
        }
        throw new ForbiddenException("Only pending or canceled events can be changed");
    }

    @Override
    public EventFull updateEventAdmin(Long eventId, EventRequestAdmin eventRequestAdmin) {
        EventFull existingEvent = eventStorage.getEventFullById(eventId); // Make sure event exists, otherwise → EventNotFoundException + Code 404
        EventFull updatedEvent = recreateEvent(existingEvent, eventRequestAdmin);
        return eventStorage.updateEventAdmin(updatedEvent);
    }

    @Override
    public EventFull addEvent(@NonNull Long userId, @NonNull EventRequest eventRequest) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        return eventStorage.addEvent(eventRequest, userId);
    }

    @Override
    public Collection<EventFull> searchEvents(@NonNull AdminSearch adminSearch) {
        return eventStorage.searchEvents(adminSearch);
    }


    @Override
    public List<EventShort> getEventsByCreator(@NonNull Long userId, int from, int size) {
        return null;
    }

    private EventFull recreateEvent(EventFull existingEvent, EventRequestAdmin eventRequestAdmin) {
        EventFull.EventFullBuilder<?, ?> fullBuilder = existingEvent.toBuilder();
        if (Objects.nonNull(eventRequestAdmin.getRequestModeration())) {
            fullBuilder.requestModeration(eventRequestAdmin.getRequestModeration());
        }
        if (Objects.nonNull(eventRequestAdmin.getParticipantLimit())) {
            fullBuilder.participantLimit(eventRequestAdmin.getParticipantLimit());
        }
        if (Objects.nonNull(eventRequestAdmin.getEventDate())) {
            fullBuilder.eventDate(eventRequestAdmin.getEventDate());
        }
        if (Objects.nonNull(eventRequestAdmin.getAnnotation())) {
            fullBuilder.annotation(eventRequestAdmin.getAnnotation());
        }
        if (Objects.nonNull(eventRequestAdmin.getPaid())) {
            fullBuilder.paid(eventRequestAdmin.getPaid());
        }
        if (Objects.nonNull(eventRequestAdmin.getTitle())) {
            fullBuilder.title(eventRequestAdmin.getTitle());
        }
        if (Objects.nonNull(eventRequestAdmin.getDescription())) {
            fullBuilder.description(eventRequestAdmin.getDescription());
        }
        if (Objects.nonNull(eventRequestAdmin.getCategory())) {
            Category category = categoryStorage.getCategoryById(eventRequestAdmin.getCategory());
            fullBuilder.category(category);
        }
        if (Objects.nonNull(eventRequestAdmin.getLocation())) {
            Location location = locationStorage.getLocationIfAbsent(eventRequestAdmin.getLocation());
            fullBuilder.location(location);
        }
        if (Objects.nonNull(eventRequestAdmin.getStateAction())) {
            StateAction stateAction = eventRequestAdmin.getStateAction();
            switch (stateAction) {
                case PUBLISH_EVENT:
                    fullBuilder.state(EventStatus.PUBLISHED);
                    fullBuilder.publishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    fullBuilder.state(EventStatus.CANCELLED);
                    break;
            }
        }
        return fullBuilder.build();
    }
}
