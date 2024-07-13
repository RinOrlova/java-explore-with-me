package ru.yandex.practicum.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.storage.participation.ParticipationEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {EventMapper.class, UserMapper.class})
public interface ParticipationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "eventId", qualifiedByName = "eventIdToEventEntity")
    @Mapping(target = "requester", source = "userId", qualifiedByName = "mapUserIdToUserEntity")
    @Mapping(target = "status", constant = "PENDING")
    ParticipationEntity mapDefaultUserEventParamsToEntity(Long userId, Long eventId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "eventId", qualifiedByName = "eventIdToEventEntity")
    @Mapping(target = "requester", source = "userId", qualifiedByName = "mapUserIdToUserEntity")
    @Mapping(target = "status", constant = "CONFIRMED")
    ParticipationEntity mapApprovedUserEventParamsToEntity(Long userId, Long eventId);

    default ParticipationEntity mapContext(@Context Long userId, @Context Long eventId) {
        return mapDefaultUserEventParamsToEntity(userId, eventId);
    }

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestResponse mapEntityToParticipationRequestResponse(ParticipationEntity entityFromStorage);
}
