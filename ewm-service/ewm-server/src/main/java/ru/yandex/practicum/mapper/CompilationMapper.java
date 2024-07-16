package ru.yandex.practicum.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.dto.compilation.UpdateCompilationRequest;
import ru.yandex.practicum.storage.compilation.CompilationEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "events", source = "events", qualifiedByName = "eventIdToEventEntity")
    CompilationEntity mapCompilatioDtoToCompilationEntity(CompilationRequest compilationRequest);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapToEventShort")
    CompilationResponse mapCompilationEntityToCompilationResponse(CompilationEntity compliationFromStorage);

    @Mapping(target = "events", source = "events", qualifiedByName = "eventShortIdToEventEntity")
    CompilationEntity mapCompilationToCompilationEntity(CompilationResponse compilationResponse);

    @Mapping(target = "events", source = "compilationRequest.events", qualifiedByName = "eventIdToEventEntity")
    @Mapping(target = "id", source = "id")
    CompilationEntity mapCompilationRequestToCompilationEntity(Long id, CompilationRequest compilationRequest);

    @Mapping(target = "events", source = "compilationRequest.events", qualifiedByName = "eventIdToEventEntity")
    @Mapping(target = "id", ignore = true)
    CompilationEntity mapUpdateCompilationRequestToCompilationEntity(Long id, UpdateCompilationRequest compilationRequest);

    default CompilationEntity mapContext(@Context Long id, CompilationRequest compilationRequest) {
        return mapCompilationRequestToCompilationEntity(id, compilationRequest);
    }

    default CompilationEntity mapContext(@Context Long id, UpdateCompilationRequest compilationRequest) {
        return mapUpdateCompilationRequestToCompilationEntity(id, compilationRequest);
    }
}
