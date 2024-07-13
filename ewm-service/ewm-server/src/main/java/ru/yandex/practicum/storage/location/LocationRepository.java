package ru.yandex.practicum.storage.location;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    LocationEntity findByLatAndLon(Double lat, Double lon);
}

