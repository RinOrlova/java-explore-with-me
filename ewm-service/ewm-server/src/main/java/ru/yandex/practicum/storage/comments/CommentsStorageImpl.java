package ru.yandex.practicum.storage.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.mapper.CommentsMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentsStorageImpl implements CommentsStorage {

    private final CommentRepository commentRepository;

    private final CommentsMapper commentsMapper;

    @Override
    public CommentResponse addComment(Long userId, CommentRequest commentRequest) {
        CommentEntity commentEntity = commentsMapper.mapCommentRequest(userId, commentRequest);
        var commentEntityFromStorage = commentRepository.saveAndFlush(commentEntity);
        return commentsMapper.mapEntityToResponse(commentEntityFromStorage);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .map(commentsMapper::mapEntityToResponse)
                .orElseThrow(() -> new EntityNotFoundException(commentId, CommentEntity.class));
    }

    @Override
    public CommentResponse updateComment(Long userId,
                                         Long commentId,
                                         UpdateCommentRequest updateCommentRequest) {
        CommentEntity commentEntity = commentsMapper.mapUpdateCommentRequest(userId, commentId, updateCommentRequest);
        CommentEntity commentFromStorage = commentRepository.findById(commentId).get();
        commentFromStorage.setText(commentEntity.getText());
        commentFromStorage.setEditedAt(LocalDateTime.now());
        CommentEntity updatedComment = commentRepository.saveAndFlush(commentFromStorage);
        return commentsMapper.mapEntityToResponse(updatedComment);
    }
}
