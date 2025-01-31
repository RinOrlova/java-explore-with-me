package ru.yandex.practicum.service.compilation;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationResponse> getCompilation(@Nullable Boolean pinned,
                                                   int from,
                                                   int size);

    CompilationResponse getCompilationById(Long id);

    CompilationResponse addCompilation(CompilationRequest compilationRequest);

    void deleteCompilation(Long id);

    CompilationResponse updateCompilation(Long id, UpdateCompilationRequest compilationRequest);

}
