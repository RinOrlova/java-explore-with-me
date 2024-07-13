package ru.yandex.practicum.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.storage.RefreshRepository;

import java.util.Collection;

public interface EventRepository extends JpaRepository<EventEntity, Long>, RefreshRepository {

    EventEntity findByIdAndInitiatorId(Long id, Long initiatorId);

    @Query("SELECT (COUNT(p) < e.participantLimit) " +
            "FROM EventEntity e " +
            "JOIN e.participationRequests p " +
            "WHERE e.id = :eventId AND p.status = 'CONFIRMED'")
    boolean canAcceptMoreParticipants(@Param("eventId") Long eventId);
}
