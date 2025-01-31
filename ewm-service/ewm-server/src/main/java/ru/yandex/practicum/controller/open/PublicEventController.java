package ru.yandex.practicum.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.dto.search.PublicSearch;
import ru.yandex.practicum.dto.search.enums.SortType;
import ru.yandex.practicum.exceptions.InvalidDateRequestedException;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(ApiPathConstants.EVENT_PATH)
public class PublicEventController {

    private final EventService eventService;

    @GetMapping()
    public Collection<EventShort> getEvents(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<@Positive Long> categoryId,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sortType", required = false) SortType sortType,
                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @PositiveOrZero Integer size) {
        if (rangeStart != null && rangeEnd != null) {
            if (!rangeStart.isBefore(rangeEnd)) {
                throw new InvalidDateRequestedException(rangeStart, rangeEnd);
            }
        }

        PublicSearch.PublicSearchBuilder<?, ?> searchBuilder = PublicSearch.builder();

        if (categoryId != null) {
            searchBuilder.categories(categoryId);
        }
        if (rangeStart != null) {
            searchBuilder.rangeStart(rangeStart);
        } else {
            searchBuilder.rangeStart(LocalDateTime.now());
        }
        if (rangeEnd != null) {
            searchBuilder.rangeEnd(rangeEnd);
        }
        if (sortType != null) {
            searchBuilder.sort(sortType);
        }
        if (StringUtils.hasText(text)) {
            searchBuilder.text(text);
        }
        if (paid != null) {
            searchBuilder.paid(paid);
        }
        PublicSearch publicSearch = searchBuilder
                .onlyAvailable(onlyAvailable)
                .from(from)
                .size(size)
                .build();
        return eventService.getEventsPublic(publicSearch);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public EventFull getEventById(@PathVariable @Positive Long id) {
        return eventService.getEventById(id);
    }
}
