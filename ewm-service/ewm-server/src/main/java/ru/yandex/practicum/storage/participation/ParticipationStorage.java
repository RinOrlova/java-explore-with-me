package ru.yandex.practicum.storage.participation;

import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ParticipationStorage {

    ParticipationRequestResponse addDefaultParticipationRequest(Long userId, Long eventId);

    ParticipationRequestResponse addApprovedParticipationRequest(Long userId, Long eventId);

    Collection<ParticipationRequestResponse> getAllRequestsForUserAndEventId(Long userId, Long eventId);

    Collection<ParticipationRequestResponse> findAllRequestsForEventOwner(Long userId, Long eventId);

    ParticipationRequestResponse getRequestById(Long requestId);

    Collection<ParticipationRequestResponse> getAllRequestsByIds(Collection<Long> requestIds);

    void declineAllRequests(Collection<Long> requestIds);

    void confirmAllRequests(List<Long> requestIds);

    void declineAllPendingRequestsForEvent(Long eventId);

    AllParticipationRequestsResponse getAllParticipationRequestsResponsesForEvent(Long eventId);

    Collection<ParticipationRequestResponse> getAllRequestsForUser(Long userId);

    ParticipationRequestResponse cancelRequest(Long requestById);

    ConfirmedRequestsProjection getConfirmedRequestsForEvent(Long eventId);

    Map<Long, Long> getAllConfirmedRequestsNumber();
}
