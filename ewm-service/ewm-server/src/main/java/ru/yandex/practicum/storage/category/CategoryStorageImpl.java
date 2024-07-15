package ru.yandex.practicum.storage.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.exceptions.CategoryNotFoundException;
import ru.yandex.practicum.exceptions.ForbiddenException;
import ru.yandex.practicum.mapper.CategoryMapper;

import java.util.Collection;
import java.util.Optional;
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
        return categoryEntityPage.stream()
                .map(categoryMapper::mapCategoryEntityToCategory)
                .collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::mapCategoryEntityToCategory)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category add(Category categoryName) {
        Optional.ofNullable(categoryRepository.findByName(categoryName.getName()))
                .ifPresent(categoryFromStorage -> {
                            throw new ForbiddenException("Category with requested name already exists.");
                        }
                );
        CategoryEntity categoryEntity = categoryMapper.mapCategoryToCategoryEntity(categoryName);
        CategoryEntity categoryFromStorage = categoryRepository.saveAndFlush(categoryEntity);
        return categoryMapper.mapCategoryEntityToCategory(categoryFromStorage);
    }

    @Override
    public Category update(Category category) {
        Optional.ofNullable(categoryRepository.findByName(category.getName()))
                .ifPresent(categoryFromStorage -> {
                            throw new ForbiddenException("Category with requested name already exists.");
                        }
                );
        CategoryEntity categoryEntity = categoryMapper.mapCategoryToCategoryEntity(category);
        CategoryEntity categoryFromStorage = categoryRepository.saveAndFlush(categoryEntity);
        return categoryMapper.mapCategoryEntityToCategory(categoryFromStorage);

    }

    @Override
    public void delete(Long catId) {
        Optional<CategoryEntity> categoryById = categoryRepository.findById(catId);
        if (categoryById.isPresent()) {
            if (categoryById.get().getEvents().isEmpty()) {
                categoryRepository.deleteById(catId);
            } else {
                throw new ForbiddenException("Not allowed to remove category with linked events.");
            }
        }
        throw new CategoryNotFoundException(catId);

    }

}
