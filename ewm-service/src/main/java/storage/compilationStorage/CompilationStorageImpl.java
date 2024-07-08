package storage.compilationStorage;

import dto.event.EventShort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CompilationStorageImpl implements CompilationStorage {


    @Override
    public Collection<EventShort> getCompilation(Boolean pinned, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventShort getCompilationById(Long id) {
        return null;
    }


}
