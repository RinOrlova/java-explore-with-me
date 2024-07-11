package ru.yandex.practicum.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventShort;
import ru.yandex.practicum.service.compilation.CompilationService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(ApiPathConstants.COMPILATION_PATH)
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService eventService;

    @GetMapping()
    public Collection<EventShort> getCompilation(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                 @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return eventService.getCompilation(pinned, from, size);
    }

    @GetMapping(ApiPathConstants.BY_ID_PATH)
    public EventShort getCompilationById(@PathVariable @Positive Long id) {
        return eventService.getCompilationById(id);
    }

}
