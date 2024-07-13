package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.storage.location.LocationEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, UserMapper.class})
public interface LocationMapper {
    @Mapping(target = "events", ignore = true)
    LocationEntity mapLocationToLocationEntity(Location location);


    Location mapLocationEntityToLocation(LocationEntity locationEntity);
}
