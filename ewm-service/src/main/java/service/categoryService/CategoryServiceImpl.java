package service.categoryService;

import dto.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import storage.categoryStorage.CategoryStorage;

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
        return null;
    }
}
