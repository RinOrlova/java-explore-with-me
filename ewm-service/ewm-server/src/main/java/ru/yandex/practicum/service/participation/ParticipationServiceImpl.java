package ru.yandex.practicum.service.participation;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;
import ru.yandex.practicum.exceptions.ConflictException;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.exceptions.ForbiddenException;
import ru.yandex.practicum.storage.event.EventStorage;
import ru.yandex.practicum.storage.participation.ParticipationStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

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
        if (!participationStorage.getAllRequestsForUserAndEventId(userId, eventId).isEmpty()) {
            throw new ConflictException("Can't create multiple participation requests from the same user.");
        }
        if (eventFull.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event initiator can't create participation requests for their event.");
        }
        if (eventFull.getState() == EventStatus.PUBLISHED) {
            if (eventFull.getParticipantLimit() > 0) {
                if (eventFull.getParticipantLimit() > eventFull.getConfirmedRequests()) {
                    return eventFull.isRequestModeration()
                            ? participationStorage.addDefaultParticipationRequest(userId, eventId)
                            : participationStorage.addApprovedParticipationRequest(userId, eventId);
                } else {
                    throw new ConflictException("Event limit reached.");
                }
            } else {
                return participationStorage.addApprovedParticipationRequest(userId, eventId);
            }
        }
        throw new ConflictException("Not allowed to create request for not published event.");
    }

    @Override
    public Collection<ParticipationRequestResponse> findAllRequestsForEventOwner(@NonNull Long userId, @NonNull Long eventId) {
        userStorage.getUserById(userId); // Make sure user exists, otherwise → UserNotFoundException + Code 404
        eventStorage.getEventFullById(eventId); // Make sure event exists, otherwise → EventNotFoundException + Code 404
        return participationStorage.findAllRequestsForEventOwner(userId, eventId);
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
                    case REJECTED:
                        return declineAllRequests(participationStatusUpdateRequest, eventId);
                    case PENDING:
                        throw new ForbiddenException("Status changing to PENDING is not allowed.");
                }
            }
        }
        throw new ForbiddenException("Requests are allowed to be moderated only by event initiator.");
    }

    private boolean allRequestsArePresentAndInStatusPending(@NonNull ParticipationStatusUpdateRequest participationStatusUpdateRequest) {

        Collection<Long> requestIds = participationStatusUpdateRequest.getRequestIds();
        Collection<ParticipationRequestResponse> allRequestsByIds = participationStorage.getAllRequestsByIds(requestIds);
        if (allRequestsByIds.size() != requestIds.size()) {
            throw new EntityNotFoundException(String.format("Not all participation requests were found by ids=%s", requestIds));
        }
        if (!areAllRequestsInStatusPending(allRequestsByIds)) {
            throw new ConflictException("Request must have status PENDING");
        }
        return true;
    }

    private boolean areAllRequestsInStatusPending(Collection<ParticipationRequestResponse> allRequestsByIds) {
        return allRequestsByIds.stream()
                .allMatch(request -> request.getStatus() == ParticipationRequestStatus.PENDING);
    }

    private AllParticipationRequestsResponse confirmAllRequests(ParticipationStatusUpdateRequest participationStatusUpdateRequest,
                                                                Long eventId) {
        List<Long> requestIds = List.copyOf(participationStatusUpdateRequest.getRequestIds());
        EventFull eventFullById = eventStorage.getEventFullById(eventId);
        if (eventFullById.getParticipantLimit() == 0) {
            participationStorage.confirmAllRequests(requestIds);
        } else {
            Long freePlacesAmount = eventStorage.getFreePlacesAmount(eventId);
            if (requestIds.size() <= freePlacesAmount) {
                participationStorage.confirmAllRequests(requestIds);
            } else {
                if (!freePlacesAmount.equals(0L)) {
                    List<Long> toBeApprovedRequests = requestIds.subList(0, Math.toIntExact(freePlacesAmount));
                    participationStorage.confirmAllRequests(toBeApprovedRequests);
                }
                participationStorage.declineAllPendingRequestsForEvent(eventId);
                throw new ConflictException("Event limit reached.");
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
