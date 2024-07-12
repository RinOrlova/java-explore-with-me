package ru.yandex.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationStatusRequest;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.exceptions.ForbiddenException;
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

    @Override
    public EventFull getEventByCreatorAndId(@NonNull Long userId, @NonNull Long eventId) {
        return eventStorage.getEventByCreatorAndId(userId, eventId);
    }

    /**
     * Обратите внимание:
     * <p>
     * изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
     */
    @Override
    public EventFull updateEvent(@NonNull Long userId, @NonNull Long eventId, @NonNull EventRequest eventRequest) {
        EventFull eventFullById = eventStorage.getEventByCreatorAndId(userId, eventId);
        if (eventFullById.getState() != EventStatus.PUBLISHED) {

        }
        throw new ForbiddenException("Only pending or canceled events can be changed");
    }

    @Override
    public EventFull addEvent(@NonNull Long userId, @NonNull EventRequest eventRequest) {
        return null;
    }

    @Override
    public ParticipationRequestResponse createParticipationRequest(@NonNull Long userId, @NonNull Long eventId) {
        return null;
    }

    @Override
    public AllParticipationRequestsResponse changeParticipationStatus(@NonNull Long userId, @NonNull Long eventId, @NonNull ParticipationStatusRequest participationStatusRequest) {
        return null;
    }


    @Override
    public List<EventShort> getEventsByCreator(@NonNull Long userId, int from, int size) {
        return null;
    }
}
