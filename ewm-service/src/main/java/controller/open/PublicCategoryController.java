package controller.open;

import dto.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.categoryService.CategoryService;
import utils.Constants;

import javax.validation.constraints.Positive;
import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.CATEGORY_PATH)
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<Category> getCategories(@RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping(Constants.BY_ID_PATH)
    public Category getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getCategoryById(id);
    }

}
