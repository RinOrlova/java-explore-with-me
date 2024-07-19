package ru.yandex.practicum.storage.location;

import ru.yandex.practicum.dto.location.Location;

public interface LocationStorage {

    LocationEntity addLocationEntityIfAbsent(Location location);

    Location getLocationIfAbsent(Location location);


}
