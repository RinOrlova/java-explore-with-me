package ru.yandex.practicum.storage.category;

import ru.yandex.practicum.dto.category.Category;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service

public class CategoryStorageImpl implements CategoryStorage {

    @Override
    public Collection<Category> getCategories(Integer from, Integer size) {
        return null;
    }

    @Override
    public Category getCategoryById(Long id) {
        return null;
    }

    @Override
    public Category add(String categoryName) {
        return null;
    }

    @Override
    public Category update(Long catId, String newName) {
        return null;
    }

    @Override
    public void delete(Long catId) {

    }

}
