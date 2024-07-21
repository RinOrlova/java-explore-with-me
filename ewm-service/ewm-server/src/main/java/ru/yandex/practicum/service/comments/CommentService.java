package ru.yandex.practicum.service.comments;

import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;

import java.util.Collection;

public interface CommentService {
    CommentResponse postComment(Long userId, CommentRequest commentRequest);

    void deleteComment(Long userId, Long commentId);

    CommentResponse updateComment(Long id, Long commentId, UpdateCommentRequest updateCommentRequest);

    void deleteComments(Collection<Long> commentsToDelete);

    Collection<CommentResponse> getCommentsByEventId(Long eventId, int from, int size);
}
