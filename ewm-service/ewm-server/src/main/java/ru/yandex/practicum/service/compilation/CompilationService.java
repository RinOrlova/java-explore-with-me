package ru.yandex.practicum.service.compilation;

import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.dto.event.EventShort;
import org.springframework.lang.Nullable;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationResponse> getCompilation(@Nullable Boolean pinned,
                                          int from,
                                          int size);

    CompilationResponse getCompilationById(Long id);

    CompilationResponse addCompilation(CompilationRequest compilationRequest);

    void deleteCompilation(Long id);

    CompilationResponse updateCompilation(Long id, CompilationRequest compilationRequest);

}
