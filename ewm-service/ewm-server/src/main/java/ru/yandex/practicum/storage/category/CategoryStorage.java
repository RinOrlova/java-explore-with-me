package ru.yandex.practicum.storage.category;

import ru.yandex.practicum.dto.category.Category;

import java.util.Collection;

public interface CategoryStorage {

    Collection<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

    Category add(Category categoryName);

    Category update(Category category);

    void delete(Long catId);
}
