package storage.categoryStorage;

import dto.category.Category;

import java.util.Collection;

public interface CategoryStorage {

    Collection<Category> getCategories(Integer from, Integer size);

    Category getCategoryById(Long id);

    Category add(String categoryName);

    Category update(Long catId, String newName);

    void delete(Long catId);
}
