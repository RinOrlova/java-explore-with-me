package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.validation.EventDateConfigurableConstraint;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
public class UpdateEventRequest {

    @Nullable
    @Size(min = 20, max = 2000)
    private String annotation;
    @Nullable
    private Long category;
    @Nullable
    @Size(min = 20, max = 7000)
    private String description;
    @Nullable
    @Future
    @EventDateConfigurableConstraint(hours = 2)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    @Nullable
    private Location location;
    @Nullable
    private Boolean paid;
    @Nullable
    @PositiveOrZero
    private Integer participantLimit;
    @Nullable
    private Boolean requestModeration;
    @Nullable
    @Size(min = 3, max = 120)
    private String title;
    @Nullable
    private StateAction stateAction;

}
