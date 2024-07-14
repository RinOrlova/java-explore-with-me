package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.common.serialization.LocalDateTimeSerializer;
import ru.yandex.practicum.dto.location.Location;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class EventFull extends EventShort {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdOn;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime publishedOn;
    private String description;
    private int participantLimit;
    private boolean requestModeration;
    private EventStatus state;
    private Location location;

    /**
     * <ul>
     * <li>Eсли для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется</li>
     * </ul>
     */
    @JsonIgnore
    public boolean isFreeToJoinEvent() {
        return !requestModeration || participantLimit == 0;
    }
}
