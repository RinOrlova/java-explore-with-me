package ru.yandex.practicum.storage.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.exceptions.CategoryNameConstraintException;
import ru.yandex.practicum.exceptions.ConflictException;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.mapper.CategoryMapper;
import ru.yandex.practicum.storage.event.EventEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryStorageImpl implements CategoryStorage {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Collection<Category> getCategories(int from, int pageSize) {
        PageRequest pageRequest = PageRequest.of(from, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<CategoryEntity> categoryEntityPage = categoryRepository.findAll(pageRequest);
        return categoryEntityPage.stream().map(categoryMapper::mapCategoryEntityToCategory).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::mapCategoryEntityToCategory)
                .orElseThrow(() -> new EntityNotFoundException(id, Category.class));
    }

    @Override
    public Category add(Category categoryName) {
        Optional.ofNullable(categoryRepository.findByName(categoryName.getName())).ifPresent(categoryFromStorage -> {
            throw new CategoryNameConstraintException();
        });
        CategoryEntity categoryEntity = categoryMapper.mapCategoryToCategoryEntity(categoryName);
        CategoryEntity categoryFromStorage = categoryRepository.saveAndFlush(categoryEntity);
        return categoryMapper.mapCategoryEntityToCategory(categoryFromStorage);
    }

    @Override
    public Category update(Category category) {
        Optional.ofNullable(categoryRepository.findByName(category.getName())).ifPresent(categoryFromStorage -> {
            if (!category.getId().equals(categoryFromStorage.getId())) {
                throw new CategoryNameConstraintException();
            }
        });
        CategoryEntity categoryEntity = categoryMapper.mapCategoryToCategoryEntity(category);
        CategoryEntity categoryFromStorage = categoryRepository.saveAndFlush(categoryEntity);
        return categoryMapper.mapCategoryEntityToCategory(categoryFromStorage);

    }

    @Override
    public void delete(Long catId) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(catId);
        if (categoryById.isPresent()) {
            Set<EventEntity> events = categoryById.get().getEvents();
            if (events == null || events.isEmpty()) {
                categoryRepository.deleteById(catId);
            } else {
                throw new ConflictException("Not allowed to remove category with linked events.");
            }
        } else {
            throw new EntityNotFoundException(catId, Category.class);
        }

    }

}
