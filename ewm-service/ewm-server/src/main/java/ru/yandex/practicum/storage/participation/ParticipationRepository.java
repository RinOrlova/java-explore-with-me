package ru.yandex.practicum.storage.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.storage.RefreshRepository;

import java.util.Collection;

public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long>, RefreshRepository {

    Collection<ParticipationEntity> findAllByRequesterIdAndEventId(Long requesterId, Long eventId);

    Collection<ParticipationEntity> findAllByEventId(Long eventId);

    @Query("SELECT p FROM ParticipationEntity p " +
            "WHERE p.requester.id = :requesterId " +
            "AND p.event.initiator.id != :requesterId")
    Collection<ParticipationEntity> findAllByRequesterIdAndEventInitiatorNotRequester(@Param("requesterId") Long requesterId);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'REJECTED' WHERE p.id IN :ids")
    int declineParticipationByIds(@Param("ids") Collection<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'CONFIRMED' WHERE p.id = :id")
    void confirmRequestById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'REJECTED' WHERE p.event.id = :eventId")
    int declineAllRequestsForEvent(@Param("eventId") Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'CANCELED' WHERE p.id = :id")
    void cancelRequestById(@Param("id") Long id);
}
