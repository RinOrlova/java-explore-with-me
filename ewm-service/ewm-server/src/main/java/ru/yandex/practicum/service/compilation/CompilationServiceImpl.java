package ru.yandex.practicum.service.compilation;

import ru.yandex.practicum.dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.storage.compilation.CompilationStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    public static CompilationStorage eventStorage;
    @Override
    public Collection<EventShort> getCompilation(@Nullable Boolean pinned,
                                                 @Nullable Integer from,
                                                 @Nullable Integer size) {
        return eventStorage.getCompilation(pinned, from, size);
    }

    @Override
    public EventShort getCompilationById(Long id) {
        return eventStorage.getCompilationById(id);
    }
}
