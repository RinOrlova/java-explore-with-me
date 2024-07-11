package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.storage.category.CategoryEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category mapCategoryEntityToCategory(CategoryEntity categoryEntity);

    CategoryEntity mapCategoryToCategoryEntity(Category category);
}
