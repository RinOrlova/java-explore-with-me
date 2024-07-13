package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.storage.compilation.CompilationEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {EventMapper.class})
public interface CompilationMapper {
    @Mapping(target = "events", source = "events", qualifiedByName = "eventIdToEventEntity")
    CompilationEntity mapCompilatioDtoToCompilationEntity(CompilationRequest compilationRequest);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapToEventShort")
    CompilationResponse mapCompilationEntityToCompilationResponse(CompilationEntity compliationFromStorage);

    @Mapping(target = "events", source = "events", qualifiedByName = "eventShortIdToEventEntity")
    CompilationEntity mapCompilationToCompilationEntity(CompilationResponse compilationResponse);

}
