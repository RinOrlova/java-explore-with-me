package ru.yandex.practicum.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.category.Category;
import ru.yandex.practicum.service.category.CategoryService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.Positive;
import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.CATEGORY_PATH)
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Collection<Category> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Category getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getCategoryById(id);
    }

}
