package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.storage.event.EventEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    EventShort mapToEventShort(EventEntity eventEntity);
    EventFull mapToEventFull(EventEntity eventEntity);

}
