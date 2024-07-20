package ru.yandex.practicum.service.comments;

import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;

public interface CommentService {
    CommentResponse postComment(@Positive @NotNull Long userId, @NotNull CommentRequest commentRequest);

    void deleteComment(@Positive @NotNull Long userId, @Positive @NotNull Long commentId);

    CommentResponse updateComment(Long id, Long commentId, UpdateCommentRequest updateCommentRequest);

    void deleteComments(Collection<Long> commentsToDelete);

    Collection<CommentResponse> getCommentsByEventId(Long eventId, int from, int size);
}
