package controller.open;

import dto.event.EventShort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.compilationService.CompilationService;
import utils.Constants;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(Constants.COMPILATION_PATH)
@RequiredArgsConstructor
public class PublicCompilationController {

    public static CompilationService eventService;

    @GetMapping()
    public Collection<EventShort> getCompilation(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                 @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getCompilation(pinned, from, size);
    }

    @GetMapping(Constants.BY_ID_PATH)
    public EventShort getCompilationById(@PathVariable @Positive Long id) {
        return eventService.getCompilationById(id);
    }

}
