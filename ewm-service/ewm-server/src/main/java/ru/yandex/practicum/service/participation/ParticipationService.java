package ru.yandex.practicum.service.participation;

import org.springframework.lang.NonNull;
import ru.yandex.practicum.dto.participation.AllParticipationRequestsResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationStatusUpdateRequest;

import java.util.Collection;

public interface ParticipationService {

    ParticipationRequestResponse createParticipationRequest(@NonNull Long userId,
                                                            @NonNull Long eventId);

    AllParticipationRequestsResponse changeParticipationStatus(@NonNull Long userId,
                                                               @NonNull Long eventId,
                                                               @NonNull ParticipationStatusUpdateRequest participationStatusUpdateRequest);

    Collection<ParticipationRequestResponse> getRequestsByUserAndEventIds(@NonNull Long id,
                                                                          @NonNull Long eventId);

    Collection<ParticipationRequestResponse> getAllRequestsForUser(Long userId);

    ParticipationRequestResponse cancelRequestForUser(Long userId, Long requestId);
}
