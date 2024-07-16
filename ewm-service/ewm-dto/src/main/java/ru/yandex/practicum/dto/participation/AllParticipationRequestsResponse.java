package ru.yandex.practicum.dto.participation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.Collection;

@Value
@Builder
@Jacksonized
public class AllParticipationRequestsResponse {
    @Builder.Default
    Collection<ParticipationRequestResponse> confirmedRequests = new ArrayList<>();
    @Builder.Default
    Collection<ParticipationRequestResponse> rejectedRequests = new ArrayList<>();

    public void addRequests(ParticipationRequestStatus status, Collection<ParticipationRequestResponse> requestResponses) {
        switch (status) {
            case REJECTED:
                rejectedRequests.addAll(requestResponses);
                break;
            case CONFIRMED:
                confirmedRequests.addAll(requestResponses);
                break;
            default:
                break;
        }
    }
}
