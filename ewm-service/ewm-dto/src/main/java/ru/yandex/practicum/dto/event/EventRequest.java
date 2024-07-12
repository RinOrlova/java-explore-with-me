package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.common.serialization.LocalDateTimeDeserializer;
import ru.yandex.practicum.common.utils.StatisticsConstants;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.dto.validation.EventDateConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRequest {

    @NotNull
    @Size(min = 20, max = 2000)
    @NotBlank
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @Size(min = 20, max = 7000)
    @NotBlank
    private String description;

    @NotNull
    @EventDateConstraint
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private boolean paid = false;

    @Min(0)
    private Integer participantLimit = 0;

    private boolean requestModeration = true;

    @NotNull
    @Size(min = 3, max = 120)
    @NotBlank
    private String title;
}

