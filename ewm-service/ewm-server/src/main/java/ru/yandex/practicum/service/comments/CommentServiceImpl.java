package ru.yandex.practicum.service.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.dto.event.EventStatus;
import ru.yandex.practicum.dto.participation.ParticipationRequestResponse;
import ru.yandex.practicum.dto.participation.ParticipationRequestStatus;
import ru.yandex.practicum.exceptions.ConflictException;
import ru.yandex.practicum.exceptions.ForbiddenException;
import ru.yandex.practicum.storage.comments.CommentsStorage;
import ru.yandex.practicum.storage.event.EventStorage;
import ru.yandex.practicum.storage.participation.ParticipationStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentsStorage commentsStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final ParticipationStorage participationStorage;

    @Override
    public CommentResponse postComment(@Positive @NotNull Long userId, CommentRequest commentRequest) {
        var eventFull = eventStorage.getEventFullById(commentRequest.getEventId()); // make sure event exists
        if (eventFull.getState() != EventStatus.PUBLISHED) {
            throw new ConflictException("Posting comments to unpublished events is not allowed.");
        }
        userStorage.getUserById(userId); // make sure user exists
        var allRequestsForUserOnEvent = participationStorage.getAllRequestsForUserAndEventId(userId, commentRequest.getEventId());
        if (anyRequestWasConfirmed(allRequestsForUserOnEvent)) {
            return commentsStorage.addComment(userId, commentRequest);
        }
        throw new ConflictException("User should have at least one approved request.");
    }

    private static boolean anyRequestWasConfirmed(Collection<ParticipationRequestResponse> allRequestsForUserOnEvent) {
        return allRequestsForUserOnEvent
                .stream()
                .anyMatch(participationRequestResponse -> participationRequestResponse.getStatus() == ParticipationRequestStatus.CONFIRMED);
    }

    @Override
    public void deleteComment(@NotNull @Positive Long userId, @Positive @NotNull Long commentId) {
        userStorage.getUserById(userId); // make sure user exists
        CommentResponse commentById = commentsStorage.getCommentById(commentId);

        if (isUserAuthorOfComment(userId, commentById)) {
            commentsStorage.deleteComment(commentId);
        }
        throw new ForbiddenException("Users can delete only their own comments.");
    }

    @Override
    public CommentResponse updateComment(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest) {
        userStorage.getUserById(userId); // make sure user exists
        CommentResponse commentById = commentsStorage.getCommentById(commentId);
        if (isUserAuthorOfComment(userId, commentById)) {
            return commentsStorage.updateComment(userId, commentId, updateCommentRequest);
        }
        throw new ForbiddenException("Users can update only their own comments.");


    }

    private static boolean isUserAuthorOfComment(Long userId, CommentResponse commentById) {
        return commentById.getUser().getId().equals(userId);
    }
}
