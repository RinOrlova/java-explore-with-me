package ru.yandex.practicum.controller.closed;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.service.comments.CommentService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.USERS_PATH + ApiPathConstants.BY_ID_PATH + ApiPathConstants.COMMENT_PATH)
public class PrivateCommentsController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse postCommentForEvent(@Positive @PathVariable(name = "id") Long id,
                                               @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.postComment(id, commentRequest);
    }

    @DeleteMapping(ApiPathConstants.COMMENT_BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive @PathVariable(name = "id") Long id,
                              @Positive @PathVariable(name = "comment_id") Long commentId) {
        commentService.deleteComment(id, commentId);
    }

    @PostMapping(ApiPathConstants.COMMENT_BY_ID_PATH)
    public CommentResponse updateComment(@Positive @PathVariable(name = "id") Long id,
                                         @Positive @PathVariable(name = "comment_id") Long commentId,
                                         @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        return commentService.updateComment(id, commentId, updateCommentRequest);
    }

}
