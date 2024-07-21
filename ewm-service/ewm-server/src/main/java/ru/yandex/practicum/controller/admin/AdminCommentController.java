package ru.yandex.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.service.comments.CommentService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@Validated
@RequestMapping(ApiPathConstants.ADMIN_PATH)
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @DeleteMapping(ApiPathConstants.COMMENT_PATH)
    public void deleteAllComments(@NotNull Collection<@Positive Long> commentsToDelete) {
        commentService.deleteComments(commentsToDelete);
    }
}
