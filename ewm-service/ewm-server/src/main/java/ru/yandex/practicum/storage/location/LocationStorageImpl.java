package ru.yandex.practicum.storage.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.location.Location;
import ru.yandex.practicum.mapper.LocationMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationStorageImpl implements LocationStorage {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationEntity addLocationEntityIfAbsent(Location location) {
        return Optional.ofNullable(locationRepository.findByLatAndLon(location.getLat(), location.getLon()))
                .orElseGet(() -> addLocation(location));
    }

    @Override
    public Location getLocationIfAbsent(Location location) {
        LocationEntity locationEntity = addLocationEntityIfAbsent(location);
        return locationMapper.mapLocationEntityToLocation(locationEntity);
    }

    public LocationEntity addLocation(Location location) {
        LocationEntity locationEntityToAdd = locationMapper.mapLocationToLocationEntity(location);
        return locationRepository.saveAndFlush(locationEntityToAdd);
    }
}
