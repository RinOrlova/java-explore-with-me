package ru.yandex.practicum.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationStatusRequest {
    private Collection<Long> requestIds;
    private ParticipationRequestStatus status;

}
