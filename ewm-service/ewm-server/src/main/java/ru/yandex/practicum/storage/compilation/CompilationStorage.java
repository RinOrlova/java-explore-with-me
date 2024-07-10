package ru.yandex.practicum.storage.compilation;

import ru.yandex.practicum.dto.event.EventShort;

import java.util.Collection;

public interface CompilationStorage {
    Collection<EventShort> getCompilation(Boolean pinned, Integer from, Integer size);

    EventShort getCompilationById(Long id);

}
