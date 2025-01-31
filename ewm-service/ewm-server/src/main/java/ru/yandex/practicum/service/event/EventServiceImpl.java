package ru.yandex.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.client.StatisticsClient;
import ru.yandex.practicum.common.dto.StatisticsResponse;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.dto.event.*;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.dto.search.PublicSearch;
import ru.yandex.practicum.exceptions.EventUpdateConflictException;
import ru.yandex.practicum.storage.category.CategoryStorage;
import ru.yandex.practicum.storage.event.EventStorage;
import ru.yandex.practicum.storage.location.LocationStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.Valid;
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
    private final StatisticsClient statisticsClient;

    @Override
    public Collection<EventShort> getEventsPublic(PublicSearch publicSearch) {
        return eventStorage.searchEventsPublic(publicSearch);
    }

    @Override
    public EventFull getEventById(Long id) {
        EventFull eventFullPublishedById = eventStorage.getEventFullPublishedById(id);
        if (eventFullPublishedById.getPublishedOn() != null) {
            Collection<StatisticsResponse> statistics = statisticsClient.getStatistics(
                    eventFullPublishedById.getPublishedOn(),
                    LocalDateTime.now(),
                    List.of("/events/" + id),
                    true
            );
            StatisticsResponse statisticsResponse = statistics.iterator().next();
            Long hits = statisticsResponse.getHits();
            eventFullPublishedById.setViews(hits);
        } else {
            eventFullPublishedById.setViews(0L);
        }
        return eventFullPublishedById;
    }

    @Override
    public EventFull getEventByCreatorAndId(@NonNull Long userId, @NonNull Long eventId) {
        return eventStorage.getEventByCreatorAndId(userId, eventId);
    }

    @Override
    public EventFull updateEvent(@NonNull Long userId, @NonNull Long eventId, @NonNull @Valid UpdateEventRequest updateEventRequest) {
        EventFull existingEvent = eventStorage.getEventByCreatorAndId(userId, eventId);
        if (existingEvent.getState() != EventStatus.PUBLISHED) {
            EventFull updatedEvent = recreateEvent(existingEvent, updateEventRequest);
            return eventStorage.updateEvent(updatedEvent, userId);
        }
        throw new EventUpdateConflictException("Only pending or canceled events can be changed");
    }

    @Override
    public EventFull updateEventAdmin(Long eventId, UpdateEventRequest updateEventRequest) {
        EventFull existingEvent = eventStorage.getEventFullById(eventId); // Make sure event exists, otherwise → EventNotFoundException + Code 404
        if (existingEvent.getState() == EventStatus.PENDING) {
            EventFull updatedEvent = recreateEvent(existingEvent, updateEventRequest);
            return eventStorage.updateEventAdmin(updatedEvent);
        }
        throw new EventUpdateConflictException(String.format("Not allowed to change event status for event=%s.", existingEvent.getState()));
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
        userStorage.getUserById(userId); // make sure user exists
        return eventStorage.getEventByCreator(userId, from, size);
    }

    private EventFull recreateEvent(EventFull existingEvent, UpdateEventRequest updateEventRequest) {
        EventFull.EventFullBuilder<?, ?> fullBuilder = existingEvent.toBuilder();
        if (Objects.nonNull(updateEventRequest.getRequestModeration())) {
            fullBuilder.requestModeration(updateEventRequest.getRequestModeration());
        }
        if (Objects.nonNull(updateEventRequest.getParticipantLimit())) {
            fullBuilder.participantLimit(updateEventRequest.getParticipantLimit());
        }
        if (Objects.nonNull(updateEventRequest.getEventDate())) {
            fullBuilder.eventDate(updateEventRequest.getEventDate());
        }
        String annotation = updateEventRequest.getAnnotation();
        if (Objects.nonNull(annotation) && StringUtils.hasText(annotation)) {
            fullBuilder.annotation(annotation);
        }
        if (Objects.nonNull(updateEventRequest.getPaid())) {
            fullBuilder.paid(updateEventRequest.getPaid());
        }
        String title = updateEventRequest.getTitle();
        if (Objects.nonNull(title) && StringUtils.hasText(title)) {
            fullBuilder.title(title);
        }
        String description = updateEventRequest.getDescription();
        if (Objects.nonNull(description) && StringUtils.hasText(description)) {
            fullBuilder.description(description);
        }
        if (Objects.nonNull(updateEventRequest.getCategory())) {
            Category category = categoryStorage.getCategoryById(updateEventRequest.getCategory());
            fullBuilder.category(category);
        }
        if (Objects.nonNull(updateEventRequest.getLocation())) {
            Location location = locationStorage.getLocationIfAbsent(updateEventRequest.getLocation());
            fullBuilder.location(location);
        }
        if (Objects.nonNull(updateEventRequest.getStateAction())) {
            StateAction stateAction = updateEventRequest.getStateAction();
            switch (stateAction) {
                case PUBLISH_EVENT:
                    fullBuilder.state(EventStatus.PUBLISHED);
                    fullBuilder.publishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                case CANCEL_REVIEW:
                    fullBuilder.state(EventStatus.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    fullBuilder.state(EventStatus.PENDING);
                    break;
            }
        }
        return fullBuilder.build();
    }
}
