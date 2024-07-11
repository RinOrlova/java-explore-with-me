package ru.yandex.practicum.storage.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.exceptions.CategoryNotFoundException;
import ru.yandex.practicum.mapper.CategoryMapper;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@EnableJpaRepositories(basePackages = "ru.yandex.practicum.storage.category")
@EntityScan(basePackages = "ru.yandex.practicum.storage.category")
public class CategoryStorageImplTest {

    private CategoryStorageImpl categoryStorage;

    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryStorage = new CategoryStorageImpl(categoryRepository, categoryMapper);
    }

    @Test
    public void testAddCategory() {
        Category category = new Category();
        category.setName("New Category");

        Category savedCategory = categoryStorage.add(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("New Category");

        // Verify the entity is saved in the repository
        CategoryEntity savedEntity = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertNotNull(savedEntity);
    }

    @Test
    public void testGetCategoryById() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Existing Category");
        categoryEntity = categoryRepository.saveAndFlush(categoryEntity);

        Category category = categoryStorage.getCategoryById(categoryEntity.getId());

        assertNotNull(category);
        assertThat(category.getName()).isEqualTo("Existing Category");
    }

    @Test
    public void testUpdateCategory() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("Original Name");
        categoryEntity = categoryRepository.saveAndFlush(categoryEntity);

        Category category = new Category();
        category.setId(categoryEntity.getId());
        category.setName("Updated Name");

        Category updatedCategory = categoryStorage.update(category);

        assertNotNull(updatedCategory);
        assertThat(updatedCategory.getName()).isEqualTo("Updated Name");

        // Verify the entity is updated in the repository
        CategoryEntity updatedEntity = categoryRepository.findById(categoryEntity.getId()).orElse(null);
        assertNotNull(updatedEntity);
        assertThat(updatedEntity.getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDeleteCategory() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("To be deleted");
        categoryEntity = categoryRepository.saveAndFlush(categoryEntity);

        categoryStorage.delete(categoryEntity.getId());

        assertThat(categoryRepository.findById(categoryEntity.getId())).isEmpty();
    }

    @Test
    public void testDeleteNonExistentUser() {
        Long nonExistentCategoryId = 999L;
        assertThatThrownBy(() -> categoryStorage.delete(nonExistentCategoryId))
                .isInstanceOf(CategoryNotFoundException.class);
    }


    @Test
    public void testGetCategories() {
        CategoryEntity category1 = new CategoryEntity();
        category1.setName("Category One");

        CategoryEntity category2 = new CategoryEntity();
        category2.setName("Category Two");

        categoryRepository.saveAll(List.of(category1, category2));

        Collection<Category> categories = categoryStorage.getCategories(0, 10);

        assertEquals(2, categories.size());
        assertThat(categories).extracting("name").containsExactlyInAnyOrder("Category One", "Category Two");
    }

}
