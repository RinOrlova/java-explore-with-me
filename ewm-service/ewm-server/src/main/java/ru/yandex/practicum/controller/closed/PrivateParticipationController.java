package ru.yandex.practicum.controller.closed;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.service.participation.ParticipationService;
import ru.yandex.practicum.utils.ApiPathConstants;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.USERS_PATH + ApiPathConstants.BY_ID_PATH + ApiPathConstants.REQUESTS_PATH)
public class PrivateParticipationController {

    private final ParticipationService participationService;

    @GetMapping
    public Collection<ParticipationRequestResponse> getRequestForUserInEvents(@PathVariable("id") Long userId) {
        return participationService.getAllRequestsForUser(userId);
    }

    @PostMapping
    public ParticipationRequestResponse createParticipationRequest(@PathVariable("id") Long userId,
                                                                   @RequestParam(value = "eventId") Long eventId) {
        return participationService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping(ApiPathConstants.REQUESTS_ID_PATH + ApiPathConstants.CANCEL_PATH)
    public ParticipationRequestResponse cancelParticipationRequest(@PathVariable("id") Long userId,
                                                                   @PathVariable(value = "requestId") Long requestId){
        return participationService.cancelRequestForUser(userId, requestId);
    }

}
