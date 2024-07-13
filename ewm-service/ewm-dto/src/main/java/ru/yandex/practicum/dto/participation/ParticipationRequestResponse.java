package ru.yandex.practicum.dto.participation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;
import ru.yandex.practicum.common.serialization.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class ParticipationRequestResponse {

    Long id;
    Long event;
    Long requester;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Builder.Default
    LocalDateTime created = LocalDateTime.now();
    @Builder.Default
    ParticipationRequestStatus status = ParticipationRequestStatus.PENDING;
}
