package storage.compilationStorage;

import dto.event.EventShort;

import java.util.Collection;

public interface CompilationStorage {
    Collection<EventShort> getCompilation(Boolean pinned, Integer from, Integer size);

    EventShort getCompilationById(Long id);

}
