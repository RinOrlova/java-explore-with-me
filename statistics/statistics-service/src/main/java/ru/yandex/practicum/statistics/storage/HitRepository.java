package ru.yandex.practicum.statistics.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<HitEntity, Long> {
}
