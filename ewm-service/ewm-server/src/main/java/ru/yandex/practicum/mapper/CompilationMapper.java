package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.compilation.CompilationRequest;
import ru.yandex.practicum.dto.compilation.CompilationResponse;
import ru.yandex.practicum.storage.compilation.CompilationEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {
    CompilationEntity mapCompilatioDtoToCompilationEntity(CompilationRequest compilationRequest);

    CompilationResponse mapCompilationEntityToCompilationResponse(CompilationEntity compliationFromStorage);
}
