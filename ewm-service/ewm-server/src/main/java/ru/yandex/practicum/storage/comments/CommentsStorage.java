package ru.yandex.practicum.storage.comments;

import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;

import java.util.Collection;

public interface CommentsStorage {
    CommentResponse addComment(Long userId, CommentRequest commentRequest);

    void deleteComment(Long commentId);

    CommentResponse getCommentById(Long commentId);

    CommentResponse updateComment(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest);

    Collection<CommentResponse> getCommentsByIds(Collection<Long> commentsToDelete);

    void deleteComments(Collection<CommentResponse> existingComments);

    Collection<CommentResponse> getCommentsByEventId(Long eventId, int from, int size);
}
