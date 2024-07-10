package controller.admin;

import ru.yandex.practicum.dto.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public Category addCategory(@Valid @RequestBody String categoryName) {
        return categoryService.addCategory(categoryName);
    }

    @PatchMapping(ApiPathConstants.BY_ID_PATH) //нужна новая константа?
    public Category updateCategory(@PathVariable @Positive Long catId,
                                   @RequestBody @Valid String newName) {
        return categoryService.updateCategory(catId, newName);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        categoryService.deleteCategory(catId);
    }


}
