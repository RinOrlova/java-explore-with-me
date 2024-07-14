package ru.yandex.practicum.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequest;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.storage.event.EventEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class})
public interface EventMapper {
    @Named("mapToEventShort")
    @Mapping(target = "initiator", source = "initiator", qualifiedByName = "mapUserEntityToUserFull")
    EventShort mapToEventShort(EventEntity eventEntity);

    @Named("eventShortIdToEventEntity")
    default EventEntity eventShortIdToEventEntity(EventShort eventShort) {
        if (eventShort == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventShort.getId());
        return eventEntity;
    }

    @Mapping(target = "initiator", source = "initiator", qualifiedByName = "mapUserEntityToUserFull")
    EventFull mapToEventFull(EventEntity eventEntity);

    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "category", source = "eventRequest.category", qualifiedByName = "mapCategoryIdToCategoryEntity")
    @Mapping(target = "initiator", source = "userId", qualifiedByName = "mapUserIdToUserEntity")
    EventEntity eventRequestToEventEntity(EventRequest eventRequest, Long userId);

    default EventEntity mapContext(EventRequest eventRequest, @Context Long userId) {
        return eventRequestToEventEntity(eventRequest, userId);
    }

    @Named("eventIdToEventEntity")
    default EventEntity eventToEventEntity(Long eventId) {
        if (eventId == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventId);
        return eventEntity;
    }

    EventEntity mapEventFullToEventEntity(EventFull updatedEvent);
}
