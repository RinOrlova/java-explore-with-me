package storage.categoryStorage;

import dto.category.Category;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service

public class CategoryStorageImpl implements CategoryStorage {

    @Override
    public Collection<Category> getCategories(Integer from, Integer size) {
        return null;
    }

}
