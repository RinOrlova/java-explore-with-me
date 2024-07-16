package ru.yandex.practicum.storage.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.exceptions.CompilationNotFoundException;
import ru.yandex.practicum.mapper.CompilationMapper;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationStorageImpl implements CompilationStorage {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationResponse addCompilation(CompilationRequest compilationRequest) {
        CompilationEntity compilationEntity = compilationMapper.mapCompilatioDtoToCompilationEntity(compilationRequest);
        CompilationEntity compilationFromStorage = compilationRepository.saveAndFlush(compilationEntity);
        compilationRepository.refresh(compilationFromStorage);
        return compilationMapper.mapCompilationEntityToCompilationResponse(compilationFromStorage);
    }

    @Override
    public CompilationResponse updateCompilation(Long id, CompilationRequest compilationRequest) {
        CompilationEntity compilationEntity = compilationMapper.mapCompilationRequestToCompilationEntity(id, compilationRequest);
        CompilationEntity compilationFromStorage = compilationRepository.saveAndFlush(compilationEntity);
        compilationRepository.refresh(compilationFromStorage);
        return compilationMapper.mapCompilationEntityToCompilationResponse(compilationFromStorage);
    }

    @Override
    public Collection<CompilationResponse> getAllCompilations(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<CompilationEntity> compilationEntityPage = compilationRepository.findAll(pageRequest);
        return mapCompilationEntitiesToCompilationResponse(compilationEntityPage);
    }

    @Override
    public Collection<CompilationResponse> getCompilation(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<CompilationEntity> compilationEntityPage = compilationRepository.findAllConsideringPinned(pinned, pageRequest);
        return mapCompilationEntitiesToCompilationResponse(compilationEntityPage);
    }

    @Override
    public CompilationResponse getCompilationById(Long id) {
        return compilationRepository.findById(id)
                .map(compilationMapper::mapCompilationEntityToCompilationResponse)
                .orElseThrow(() -> new CompilationNotFoundException(id));
    }

    @Override
    public void deleteCompilation(Long id) {
        try {
            compilationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            log.warn("Compilation not found by requested id:{}", id);
            throw new CompilationNotFoundException(id);
        }
    }


    private Collection<CompilationResponse> mapCompilationEntitiesToCompilationResponse(Page<CompilationEntity> compilationEntityPage) {
        return compilationEntityPage.stream()
                .map(compilationMapper::mapCompilationEntityToCompilationResponse)
                .collect(Collectors.toList());
    }

}
