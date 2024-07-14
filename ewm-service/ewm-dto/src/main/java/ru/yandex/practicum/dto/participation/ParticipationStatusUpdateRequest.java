package ru.yandex.practicum.dto.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationStatusUpdateRequest {
    @UniqueElements
    private Collection<Long> requestIds;
    private ParticipationRequestStatus status;
}
