package ru.yandex.practicum.storage.category;

import lombok.*;
import ru.yandex.practicum.storage.event.EventEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"events"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "cat_name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private Set<EventEntity> events;
}
