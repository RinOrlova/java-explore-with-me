package ru.yandex.practicum.service.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.exceptions.ForbiddenException;
import ru.yandex.practicum.storage.event.EventStorage;
import ru.yandex.practicum.storage.participation.ParticipationStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationStorage participationStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    @Override
    public ParticipationRequestResponse createParticipationRequest(@NonNull Long userId,
                                                                   @NonNull Long eventId) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        EventFull eventFull = eventStorage.getEventFullById(eventId); // Make sure event exists, otherwise → EventNotFoundException + Code 404
        if (eventFull.getState() == EventStatus.PUBLISHED) {
            return eventFull.isFreeToJoinEvent()
                    ? participationStorage.addApprovedParticipationRequest(userId, eventId)
                    : participationStorage.addDefaultParticipationRequest(userId, eventId);
        }
        throw new ForbiddenException("Not allowed to create request for not published event.");
    }

    @Override
    public Collection<ParticipationRequestResponse> getRequestsByUserAndEventIds(@NonNull Long userId, @NonNull Long eventId) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        eventStorage.getEventFullById(eventId); // Make sure event exists, otherwise → EventNotFoundException + Code 404
        return participationStorage.getAllRequestsForUserAndEventId(userId, eventId);
    }

    @Override
    public Collection<ParticipationRequestResponse> getAllRequestsForUser(Long userId) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        return participationStorage.getAllRequestsForUser(userId);
    }

    @Override
    public ParticipationRequestResponse cancelRequestForUser(Long userId, Long requestId) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        ParticipationRequestResponse requestById = participationStorage.getRequestById(requestId); // Make sure user exists, otherwise → ParticipationNotFoundException + Code 404
        if (requestById.getRequester().equals(userId)) {
            return participationStorage.cancelRequest(requestId);
        }
        throw new ForbiddenException("Not allowed to cancel requests of other users.");
    }

    /**
     * <ul>
     * <li>Hельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)</li>
     * <li>Cтатус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)</li>
     * <li>Eсли при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить</li>
     * </ul>
     */
    @Override
    public AllParticipationRequestsResponse changeParticipationStatus(@NonNull Long userId,
                                                                      @NonNull Long eventId,
                                                                      @NonNull ParticipationStatusUpdateRequest participationStatusUpdateRequest) {
        if (eventStorage.isUserOwnerOfEvent(userId, eventId)) {
            if (allRequestsArePresentAndInStatusPending(participationStatusUpdateRequest)) {
                switch (participationStatusUpdateRequest.getStatus()) {
                    case CONFIRMED:
                        return confirmAllRequests(participationStatusUpdateRequest, eventId);
                    case DECLINED:
                        return declineAllRequests(participationStatusUpdateRequest, eventId);
                    case PENDING:
                        throw new ForbiddenException("Status changing to PENDING is not allowed.");
                }
            }
        }
        throw new ForbiddenException("Requests are allowed to be moderated only by event initiator.");
    }

    private boolean allRequestsArePresentAndInStatusPending(@NonNull ParticipationStatusUpdateRequest participationStatusUpdateRequest) {
        for (Long requestId : participationStatusUpdateRequest.getRequestIds()) {
            if (!participationStorage.isRequestPresentInStatusPending(requestId)) {
                throw new ForbiddenException("Request must have status PENDING");
            }
        }
        return true;
    }

    private AllParticipationRequestsResponse confirmAllRequests(ParticipationStatusUpdateRequest participationStatusUpdateRequest,
                                                                Long eventId) {
        for (Long requestId : participationStatusUpdateRequest.getRequestIds()) {
            if (eventStorage.anyFreePlacesLeft(eventId)) {
                participationStorage.confirmRequest(requestId);
            } else {
                participationStorage.declineAllPendingRequestsForEvent(eventId);
                break;
            }
        }
        return participationStorage.getAllParticipationRequestsResponsesForEvent(eventId);
    }

    private AllParticipationRequestsResponse declineAllRequests(ParticipationStatusUpdateRequest updateRequest,
                                                                Long eventId) {
        participationStorage.declineAllRequests(updateRequest.getRequestIds());
        return participationStorage.getAllParticipationRequestsResponsesForEvent(eventId);
    }
}
