package ru.yandex.practicum.dto.participation;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collection;

@Value
@Builder
public class AllParticipationRequestsResponse {

    Collection<ParticipationRequestResponse> confirmedRequests = new ArrayList<>();
    Collection<ParticipationRequestResponse> rejectedRequests = new ArrayList<>();

}
