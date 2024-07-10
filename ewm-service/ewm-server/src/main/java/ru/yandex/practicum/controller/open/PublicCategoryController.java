package ru.yandex.practicum.controller.open;

import ru.yandex.practicum.dto.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.category.CategoryService;
import ru.yandex.practicum.utils.ApiPathConstants;
import ru.yandex.practicum.stats.StatisticsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.CATEGORY_PATH)
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final StatisticsService statisticsService;

    @GetMapping
    public Collection<Category> getCategories(@RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                              HttpServletRequest httpServletRequest) {
        statisticsService.sendHitRequest(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return categoryService.getCategories(from, size);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public Category getCategoryById(@PathVariable @Positive Long id,
                                    HttpServletRequest httpServletRequest) {
        statisticsService.sendHitRequest(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return categoryService.getCategoryById(id);
    }

}
