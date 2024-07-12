package ru.yandex.practicum.service.event;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationStatusRequest;
import ru.yandex.practicum.enums.Sort;

import java.util.Collection;
import java.util.List;

public interface EventService {

    Collection<EventShort> getEvents(String text,
                                     @Nullable List<Integer> categoryId,
                                     @Nullable Boolean paid,
                                     @Nullable String rangeStart,
                                     @Nullable String rangeEnd,
                                     boolean onlyAvailable,
                                     @Nullable Sort sort,
                                     Integer from,
                                     Integer size);

    EventShort getEventById(Long id);

    EventFull getEventByCreatorAndId(@NonNull Long userId,
                                     @NonNull Long eventId);

    EventFull updateEvent(@NonNull Long userId,
                          @NonNull Long eventId,
                          @NonNull EventRequest eventRequest);

    List<EventShort> getEventsByCreator(@NonNull Long userId,
                                        int from,
                                        int size);

    EventFull addEvent(@NonNull Long userId,
                       @NonNull EventRequest eventRequest);

    ParticipationRequestResponse createParticipationRequest(@NonNull Long userId,
                                                            @NonNull Long eventId);
    AllParticipationRequestsResponse changeParticipationStatus(@NonNull Long userId,
                                                               @NonNull Long eventId,
                                                               @NonNull ParticipationStatusRequest participationStatusRequest);
}
