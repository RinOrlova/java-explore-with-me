package controller.open;

import dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.eventService.EventService;
import utils.Constants;
import utils.Sort;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.EVENT_PATH)
public class PublicEventController {

    private static EventService eventService;

    @GetMapping()
    public Collection<EventShort> getEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories", required = false) ArrayList<Integer> categoryId,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(name = "onlyAvailable", required = false) String onlyAvailable,
                                            @RequestParam(name = "sort", required = false) Sort sort,
                                            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getEvents(text, categoryId, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(Constants.BY_ID_PATH)
    public EventShort getEventById(@PathVariable @Positive Long id) {
        return eventService.getEventById(id);
    }

}
