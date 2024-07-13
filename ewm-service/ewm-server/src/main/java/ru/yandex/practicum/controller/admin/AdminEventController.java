package ru.yandex.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventFull;
import ru.yandex.practicum.dto.event.EventRequestAdmin;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.search.AdminSearch;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.ADMIN_PATH + ApiPathConstants.EVENT_PATH)
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public Collection<EventFull> getAllEvents(
            @RequestParam(name = "users", required = false) List<Long> userIds,
            @RequestParam(name = "states", required = false) List<EventStatus> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        AdminSearch.AdminSearchBuilder<?, ?> adminSearchBuilder = AdminSearch.builder();
        if (userIds != null) {
            adminSearchBuilder.userIds(userIds);
        }
        if (states != null) {
            adminSearchBuilder.states(states);
        }
        if (categories != null) {
            adminSearchBuilder.categories(categories);
        }
        if (rangeStart != null) {
            adminSearchBuilder.rangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            adminSearchBuilder.rangeStart(rangeEnd);
        }
        AdminSearch adminSearch = adminSearchBuilder
                .from(from)
                .size(size)
                .build();

        return eventService.searchEvents(adminSearch);
    }

    @PatchMapping(ApiPathConstants.BY_ID_PATH)
    public EventFull updateEventState(@PathVariable("id") Long eventId,
                                      @Valid @RequestBody EventRequestAdmin eventRequestAdmin) {
        return eventService.updateEventAdmin(eventId, eventRequestAdmin);
    }

}
