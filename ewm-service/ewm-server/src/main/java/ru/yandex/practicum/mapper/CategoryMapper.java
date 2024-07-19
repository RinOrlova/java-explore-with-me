package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.storage.category.CategoryEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category mapCategoryEntityToCategory(CategoryEntity categoryEntity);

    CategoryEntity mapCategoryToCategoryEntity(Category category);

    @Named("mapCategoryIdToCategoryEntity")
    default CategoryEntity mapCategoryIdToCategoryEntity(Long id) {
        if (id == null) {
            return null;
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(id);
        return categoryEntity;
    }
}
