package service.compilationService;

import dto.event.EventShort;
import org.springframework.lang.Nullable;

import java.util.Collection;

public interface CompilationService {
    Collection<EventShort> getCompilation(@Nullable Boolean pinned,
                                          @Nullable Integer from,
                                          @Nullable Integer size);

    EventShort getCompilationById(Long id);

}
