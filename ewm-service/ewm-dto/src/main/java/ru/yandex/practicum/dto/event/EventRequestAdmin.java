package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;
import ru.yandex.practicum.dto.location.Location;

import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class EventRequestAdmin {

    @Nullable
    private String annotation;
    @Nullable
    private Long category;
    @Nullable
    private String description;
    @Nullable
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    @Nullable
    private Location location;
    @Nullable
    private Boolean paid;
    @Nullable
    private Integer participantLimit;
    @Nullable
    private Boolean requestModeration;
    @Nullable
    private String title;
    @Nullable
    private StateAction stateAction;

}
