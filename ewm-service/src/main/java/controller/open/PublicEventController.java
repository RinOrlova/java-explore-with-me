package controller.open;

import dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.eventService.EventService;
import utils.Constants;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.EVENT_PATH)
public class PublicEventController {

    private static EventService eventService;

    @GetMapping()
    public Collection<EventShort> getEvents(@RequestParam(name = "text") String text,
                                            @RequestParam(name = "categories", required = false) String text,)

}
