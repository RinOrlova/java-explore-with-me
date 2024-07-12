package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.storage.event.EventEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    @Named("mapToEventShort")
    EventShort mapToEventShort(EventEntity eventEntity);

    EventFull mapToEventFull(EventEntity eventEntity);

    @Named("eventIdToEventEntity")
    default EventEntity eventToEventEntity(Long eventId) {
        if (eventId == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventId);
        return eventEntity;
    }

}
