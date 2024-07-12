package ru.yandex.practicum.storage.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    EventEntity findByIdAndInitiatorId(Long id, Long initiatorId);

}
