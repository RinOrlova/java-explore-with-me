package ru.yandex.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.storage.category.CategoryStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;

    @Override
    public Collection<Category> getCategories(int from, int size) {
        return categoryStorage.getCategories(from, size);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryStorage.getCategoryById(id);
    }

    @Override
    public Category addCategory(Category categoryName) {
        return categoryStorage.add(categoryName);
    }

    @Override
    public Category updateCategory(Long catId, Category updatedCategory) {
        Category categoryById = getCategoryById(catId);
        categoryById.setName(updatedCategory.getName());
        return categoryStorage.update(categoryById);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryStorage.delete(catId);
    }
}
