package ru.yandex.practicum.storage.event;

import lombok.*;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.storage.category.CategoryEntity;
import ru.yandex.practicum.storage.compilation.CompilationEntity;
import ru.yandex.practicum.storage.location.LocationEntity;
import ru.yandex.practicum.storage.participation.ParticipationEntity;
import ru.yandex.practicum.storage.user.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(exclude = "participationRequests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 2000)
    private String annotation;

    @NotNull
    @Column(nullable = false, length = 7000)
    private String description;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    // TODO: publishedOn should null until its status changes to PUBLISHED
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private int participantLimit;

    @Column(name = "request_moderation", nullable = false)
    private boolean requestModeration;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @NotNull
    @Column(nullable = false, length = 120)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private UserEntity initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    private Set<CompilationEntity> compilations;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    private Set<ParticipationEntity> participationRequests;

    @Transient
    private long views;
    @Transient
    private long confirmedRequests;

}