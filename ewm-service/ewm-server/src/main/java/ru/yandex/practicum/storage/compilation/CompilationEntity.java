package ru.yandex.practicum.storage.compilation;

import lombok.*;
import ru.yandex.practicum.storage.event.EventEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@EqualsAndHashCode(exclude = "events")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean pinned;

    @NotNull
    @Column(nullable = false, length = 7000)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilation_event_views",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<EventEntity> events;


}
