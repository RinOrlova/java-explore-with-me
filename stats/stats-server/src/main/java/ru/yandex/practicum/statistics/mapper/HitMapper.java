package ru.yandex.practicum.statistics.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.common.dto.HitRequest;
import ru.yandex.practicum.statistics.storage.HitEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface HitMapper {
    @Mapping(target = "id", ignore = true)
    HitEntity map(HitRequest hitRequest);

}
