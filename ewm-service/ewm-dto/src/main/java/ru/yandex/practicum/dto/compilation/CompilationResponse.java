package ru.yandex.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.event.EventShort;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationResponse {

    Collection<EventShort> events;
    private Long id;
    private boolean pinned;
    private String title;


}
