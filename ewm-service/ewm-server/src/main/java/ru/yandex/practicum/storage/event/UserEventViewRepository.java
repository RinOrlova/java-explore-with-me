package ru.yandex.practicum.storage.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventViewRepository extends JpaRepository<UserEventView, Long> {

    long countByEvent(EventEntity event);
}