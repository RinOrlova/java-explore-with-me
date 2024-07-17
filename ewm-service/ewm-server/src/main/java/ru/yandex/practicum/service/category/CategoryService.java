package ru.yandex.practicum.service.category;

import ru.yandex.practicum.dto.category.Category;

import java.util.Collection;

public interface CategoryService {

    Collection<Category> getCategories(int from,
                                       int size);

    Category getCategoryById(Long id);

    Category addCategory(Category categoryName);

    Category updateCategory(Long catId, Category newName);

    void deleteCategory(Long catId);

}
