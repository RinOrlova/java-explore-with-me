package ru.yandex.practicum.storage.compilation;

import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.Collection;

public interface CompilationStorage {

    Collection<CompilationResponse> getAllCompilations(int from, int size);

    Collection<CompilationResponse> getCompilation(boolean pinned, int from, int size);

    CompilationResponse getCompilationById(Long id);

    void deleteCompilation(Long id);

    CompilationResponse addCompilation(CompilationRequest compilationRequest);

    CompilationResponse updateCompilation(Long id, UpdateCompilationRequest compilationRequest);
}
