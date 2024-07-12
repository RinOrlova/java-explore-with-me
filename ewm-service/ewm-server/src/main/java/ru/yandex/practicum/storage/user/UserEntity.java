package ru.yandex.practicum.storage.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.storage.event.EventEntity;
import ru.yandex.practicum.storage.participation.ParticipationEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "requester")
    private Collection<ParticipationEntity> participationRequest;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "initiator")
    private Set<EventEntity> events;

}
