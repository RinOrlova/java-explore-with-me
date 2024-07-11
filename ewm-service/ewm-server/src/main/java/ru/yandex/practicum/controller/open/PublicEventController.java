package ru.yandex.practicum.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.EVENT_PATH)
public class PublicEventController {

    private final EventService eventService;

    @GetMapping()
    public Collection<EventShort> getEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryId,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sort", required = false) Sort sort,
                                            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getEvents(text, categoryId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public EventShort getEventById(@PathVariable @Positive Long id) {
        return eventService.getEventById(id);
    }

}
