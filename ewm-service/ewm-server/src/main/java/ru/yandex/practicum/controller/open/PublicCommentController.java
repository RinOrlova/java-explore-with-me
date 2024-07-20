package ru.yandex.practicum.controller.open;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.service.comments.CommentService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController(ApiPathConstants.COMMENT_PATH)
@RequiredArgsConstructor
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping(ApiPathConstants.EVENT_BY_ID_PATH)
    public Collection<CommentResponse> getCommentsByEventId(@PathVariable(name = "eventId") @NotNull @Positive Long eventId,
                                                            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return commentService.getCommentsByEventId(eventId, from, size);
    }
}
