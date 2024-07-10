package ru.yandex.practicum.controller.open;

import ru.yandex.practicum.dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.utils.ApiPathConstants;
import ru.yandex.practicum.enums.Sort;
import ru.yandex.practicum.stats.StatisticsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.EVENT_PATH)
public class PublicEventController {

    private final EventService eventService;
    private final StatisticsService statisticsService;

    @GetMapping()
    public Collection<EventShort> getEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories", required = false) List<Integer> categoryId,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sort", required = false) Sort sort,
                                            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                            HttpServletRequest httpServletRequest) {
        statisticsService.sendHitRequest(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return eventService.getEvents(text, categoryId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public EventShort getEventById(@PathVariable @Positive Long id, HttpServletRequest httpServletRequest) {
        statisticsService.sendHitRequest(httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr());
        return eventService.getEventById(id);
    }

}
