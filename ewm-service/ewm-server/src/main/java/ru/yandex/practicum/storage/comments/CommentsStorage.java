package ru.yandex.practicum.storage.comments;

import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;

public interface CommentsStorage {
    CommentResponse addComment(Long userId, CommentRequest commentRequest);

    void deleteComment(Long commentId);

    CommentResponse getCommentById(Long commentId);

    CommentResponse updateComment(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest);
}
