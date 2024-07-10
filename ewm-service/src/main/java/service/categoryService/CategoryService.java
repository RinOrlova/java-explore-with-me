package service.categoryService;

import dto.category.Category;
import org.springframework.lang.Nullable;

import java.util.Collection;

public interface CategoryService {

    Collection<Category> getCategories(@Nullable Integer from,
                                       @Nullable Integer size);

    Category getCategoryById(Long id);

    Category addCategory(String categoryName);

    Category updateCategory(Long catId, String newName);

    void deleteCategory(Long catId);

}
