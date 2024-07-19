package ru.yandex.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.service.category.CategoryService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.ADMIN_PATH + ApiPathConstants.CATEGORY_PATH)
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@Valid @RequestBody Category categoryName) {
        return categoryService.addCategory(categoryName);
    }

    @PatchMapping(ApiPathConstants.BY_ID_PATH)
    public Category updateCategory(@PathVariable @Positive Long id,
                                   @RequestBody @Valid Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteCategory(id);
    }


}
