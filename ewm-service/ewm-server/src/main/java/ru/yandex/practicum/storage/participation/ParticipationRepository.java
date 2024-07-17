package ru.yandex.practicum.storage.participation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.storage.RefreshRepository;

import java.util.Collection;
import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationEntity, Long>, RefreshRepository {

    @Query("SELECT p FROM ParticipationEntity p WHERE p.requester.id = :requesterId AND p.event.id = :eventId")
    Collection<ParticipationEntity> findAllByUserInEvent(Long requesterId, Long eventId);

    Collection<ParticipationEntity> findAllByEventId(Long eventId);

    @Query("SELECT p FROM ParticipationEntity p " +
            "WHERE p.requester.id = :requesterId " +
            "AND p.event.initiator.id != :requesterId")
    Collection<ParticipationEntity> findAllByRequesterIdAndEventInitiatorNotRequester(@Param("requesterId") Long requesterId);

    @Query("SELECT p FROM ParticipationEntity p " +
            "WHERE p.event.id = :eventId " +
            "AND p.event.initiator.id = :requesterId")
    Collection<ParticipationEntity> findAllRequestsForEventOwner(@Param("requesterId") Long requesterId, @Param("eventId") Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'REJECTED' WHERE p.id IN :ids")
    int declineParticipationByIds(@Param("ids") Collection<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'REJECTED' WHERE p.event.id = :eventId")
    int declineAllRequestsForEvent(@Param("eventId") Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'CANCELED' WHERE p.id = :id")
    void cancelRequestById(@Param("id") Long id);

    @Query("SELECT p.event.id AS eventId, COUNT(p) AS confirmedRequestsNumber " +
            "FROM ParticipationEntity p " +
            "WHERE p.status = 'CONFIRMED' AND p.event.id = :eventId " +
            "GROUP BY p.event.id")
    ConfirmedRequestsProjection getConfirmedRequestsForEventNumber(@Param("eventId") Long eventId);


    @Query("SELECT p.event.id AS eventId, COUNT(p) AS confirmedRequestsNumber " +
            "FROM ParticipationEntity p " +
            "WHERE p.status = 'CONFIRMED' " +
            "GROUP BY p.event.id")
    Collection<ConfirmedRequestsProjection> getAllConfirmedRequestsNumber();

    @Modifying
    @Transactional
    @Query("UPDATE ParticipationEntity p SET p.status = 'CONFIRMED' WHERE p.id IN :requestIds")
    int confirmParticipationByIds(List<Long> requestIds);
}
