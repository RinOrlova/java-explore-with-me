package controller.admin;

import dto.category.Category;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import service.categoryService.CategoryService;
import utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.ADMIN_PATH + Constants.CATEGORY_PATH)
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@Valid @RequestBody String categoryName) {
        return categoryService.addCategory(categoryName);
    }

    @PatchMapping(Constants.BY_ID_PATH) //нужна новая константа?
    public Category updateCategory(@PathVariable @Positive Long catId,
                                   @RequestBody @Valid String newName) {
        return categoryService.updateCategory(catId, newName);
    }

    @DeleteMapping(Constants.BY_ID_PATH)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        categoryService.deleteCategory(catId);
    }


}
