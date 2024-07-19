package ru.yandex.practicum.storage.participation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.storage.event.EventEntity;
import ru.yandex.practicum.storage.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity requester;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStatus status;

}
