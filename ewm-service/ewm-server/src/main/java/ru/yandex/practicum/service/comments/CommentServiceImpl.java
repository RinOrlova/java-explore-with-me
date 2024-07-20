package ru.yandex.practicum.service.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.dto.event.EventFull;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentsStorage commentsStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final ParticipationStorage participationStorage;

    private static boolean anyRequestWasConfirmed(Collection<ParticipationRequestResponse> allRequestsForUserOnEvent) {
        return allRequestsForUserOnEvent
                .stream()
                .anyMatch(participationRequestResponse -> participationRequestResponse.getStatus() == ParticipationRequestStatus.CONFIRMED);
    }

    private static boolean isUserAuthorOfComment(Long userId, CommentResponse commentById) {
        return commentById.getUserId().equals(userId);
    }

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

    @Override
    public void deleteComment(@NotNull @Positive Long userId, @Positive @NotNull Long commentId) {
        userStorage.getUserById(userId); // make sure user exists
        CommentResponse commentById = commentsStorage.getCommentById(commentId);

        if (isUserAuthorOfComment(userId, commentById)) {
            commentsStorage.deleteComment(commentId);
            return;
        }
        throw new ForbiddenException("Users can delete only their own comments.");
    }

    @Override
    public CommentResponse updateComment(Long userId, Long commentId, UpdateCommentRequest updateCommentRequest) {
        userStorage.getUserById(userId);
        CommentResponse commentById = commentsStorage.getCommentById(commentId);
        if (isUserAuthorOfComment(userId, commentById)) {
            return commentsStorage.updateComment(userId, commentId, updateCommentRequest);
        }
        throw new ForbiddenException("Users can update only their own comments.");


    }

    @Override
    public void deleteComments(Collection<Long> commentsToDelete) {
        Collection<CommentResponse> existingComments = commentsStorage.getCommentsByIds(commentsToDelete);
        Set<Long> existingIds = existingComments.stream()
                .map(CommentResponse::getId)
                .collect(Collectors.toSet());
        Collection<Long> notExistingIds = new ArrayList<>();
        for (Long id : commentsToDelete) {
            if (!existingIds.contains(id)) {
                notExistingIds.add(id);
            }
        }
        if (!notExistingIds.isEmpty()) {
            log.warn("Comments with ids {} are not found", notExistingIds);
        }
        if (!existingIds.isEmpty()) {
            commentsStorage.deleteComments(existingComments);
        }
    }

    @Override
    public Collection<CommentResponse> getCommentsByEventId(Long eventId, int from, int size) {
        EventFull eventToCheck = eventStorage.getEventFullById(eventId);
        if (eventToCheck.getState() != EventStatus.PUBLISHED) {
            return Collections.emptyList();
        }
        return commentsStorage.getCommentsByEventId(eventId, from, size);
    }
}
