package ru.yandex.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.dto.compilation.UpdateCompilationRequest;
import ru.yandex.practicum.storage.compilation.CompilationStorage;
import ru.yandex.practicum.storage.event.EventStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;

    @Override
    public Collection<CompilationResponse> getCompilation(@Nullable Boolean pinned,
                                                          int from,
                                                          int size) {
        if (pinned == null) {
            return compilationStorage.getAllCompilations(from, size);
        }
        return compilationStorage.getCompilation(pinned, from, size);
    }

    @Override
    public CompilationResponse getCompilationById(Long id) {
        return compilationStorage.getCompilationById(id);
    }

    @Override
    public CompilationResponse addCompilation(CompilationRequest compilationRequest) {
        if (compilationRequest.getEvents() != null) {
            for (Long eventId : compilationRequest.getEvents()) {
                eventStorage.getEventShortById(eventId);
            }
        }
        return compilationStorage.addCompilation(compilationRequest);
    }

    @Override
    public void deleteCompilation(Long id) {
        compilationStorage.deleteCompilation(id);
    }

    @Override
    public CompilationResponse updateCompilation(Long id, @Valid UpdateCompilationRequest compilationRequest) {
        compilationStorage.getCompilationById(id); // Make sure compilation exists
        if (compilationRequest.getEvents() != null) {
            for (Long eventId : compilationRequest.getEvents()) {
                eventStorage.getEventShortById(eventId);
            }
        }
        return compilationStorage.updateCompilation(id, compilationRequest);
    }


}
