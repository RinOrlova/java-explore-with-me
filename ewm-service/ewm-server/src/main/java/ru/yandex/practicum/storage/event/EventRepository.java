package ru.yandex.practicum.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.storage.RefreshRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long>, RefreshRepository {

    EventEntity findByIdAndInitiatorId(Long id, Long initiatorId);

    @Query("SELECT e FROM EventEntity e WHERE e.id = :id AND e.status='PUBLISHED'")
    EventEntity findByIdAndStatusPublished(Long id);

    Page<EventEntity> findByInitiatorId(Long id, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(p) < e.participantLimit THEN TRUE ELSE FALSE END " +
            "FROM EventEntity e " +
            "JOIN e.participationRequests p " +
            "WHERE e.id = :eventId AND p.status = 'CONFIRMED'")
    boolean canAcceptMoreParticipants(@Param("eventId") Long eventId);
}
