package ru.yandex.practicum.service.category;

import ru.yandex.practicum.dto.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.storage.category.CategoryStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;

    @Override
    public Collection<Category> getCategories(@Nullable Integer from, @Nullable Integer size) {
        return categoryStorage.getCategories(from, size);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryStorage.getCategoryById(id);
    }

    @Override
    public Category addCategory(String categoryName) {
        return categoryStorage.add(categoryName);
    }

    @Override
    public Category updateCategory(Long catId, String newName) {
        return categoryStorage.update(catId, newName);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryStorage.delete(catId);
    }
}
