package storage.categoryStorage;

import dto.category.Category;

import java.util.Collection;

public interface CategoryStorage {

    Collection<Category> getCategories(Integer from, Integer size);

}
