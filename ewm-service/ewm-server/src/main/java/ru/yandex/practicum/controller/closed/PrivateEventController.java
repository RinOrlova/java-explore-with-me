package ru.yandex.practicum.controller.closed;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.event.UpdateEventRequest;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.service.participation.ParticipationService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.USERS_PATH + ApiPathConstants.BY_ID_PATH + ApiPathConstants.EVENT_PATH)
public class PrivateEventController {

    private final EventService eventService;
    private final ParticipationService participationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFull addEvent(@PathVariable("id") Long userId,
                              @Valid @RequestBody EventRequest eventRequest) {
        return eventService.addEvent(userId, eventRequest);
    }

    @GetMapping
    public List<EventShort> getEventsByCreator(@PathVariable("id") Long userId,
                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return eventService.getEventsByCreator(userId, from, size);
    }

    @GetMapping(ApiPathConstants.EVENT_BY_ID_PATH)
    public EventFull getEventByCreatorAndId(@PathVariable("id") Long id,
                                            @PathVariable("eventId") Long eventId) {
        return eventService.getEventByCreatorAndId(id, eventId);
    }

    @PatchMapping(ApiPathConstants.EVENT_BY_ID_PATH)
    public EventFull updateEvent(@PathVariable("id") Long id,
                                 @PathVariable("eventId") Long eventId,
                                 @Valid @RequestBody UpdateEventRequest eventRequest) {
        return eventService.updateEvent(id, eventId, eventRequest);
    }

    @GetMapping(ApiPathConstants.EVENT_BY_ID_PATH + ApiPathConstants.REQUESTS_PATH)
    public Collection<ParticipationRequestResponse> getRequestsByUserAndEventIds(@PathVariable("id") Long id,
                                                                                 @PathVariable("eventId") Long eventId) {
        return participationService.findAllRequestsForEventOwner(id, eventId);
    }

    @PatchMapping(ApiPathConstants.EVENT_BY_ID_PATH + ApiPathConstants.REQUESTS_PATH)
    public AllParticipationRequestsResponse changeParticipationStatus(@PathVariable("id") Long id,
                                                                      @PathVariable("eventId") Long eventId,
                                                                      @Valid @RequestBody ParticipationStatusUpdateRequest participationStatusUpdateRequest) {
        return participationService.changeParticipationStatus(id, eventId, participationStatusUpdateRequest);
    }


}
