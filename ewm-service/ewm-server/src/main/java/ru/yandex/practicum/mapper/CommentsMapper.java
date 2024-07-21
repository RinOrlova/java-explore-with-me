package ru.yandex.practicum.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.yandex.practicum.dto.comments.CommentRequest;
import ru.yandex.practicum.dto.comments.CommentResponse;
import ru.yandex.practicum.dto.comments.UpdateCommentRequest;
import ru.yandex.practicum.storage.comments.CommentEntity;
import ru.yandex.practicum.storage.event.EventEntity;
import ru.yandex.practicum.storage.user.UserEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface CommentsMapper {
    @Mapping(target = "author", source = "userId", qualifiedByName = "mapUserIdToUserEntity")
    @Mapping(target = "event", source = "commentRequest.eventId", qualifiedByName = "mapEntityId")
    @Mapping(target = "created", ignore = true)
    CommentEntity mapCommentRequest(Long userId, CommentRequest commentRequest);

    @Mapping(target = "author", source = "userId", qualifiedByName = "mapUserIdToUserEntity")
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "editedAt", ignore = true)
    @Mapping(target = "created", ignore = true)
    CommentEntity mapUpdateCommentRequest(Long userId, Long eventId, UpdateCommentRequest commentRequest);

    default CommentEntity mapContext(@Context Long userId, CommentRequest commentRequest) {
        return mapCommentRequest(userId, commentRequest);
    }

    default CommentEntity mapContext(@Context Long userId, @Context Long eventId, UpdateCommentRequest commentRequest) {
        return mapUpdateCommentRequest(userId, eventId, commentRequest);
    }

    @Mapping(target = "eventId", source = "event", qualifiedByName = "mapEventEntityToId")
    @Mapping(target = "userId", source = "author", qualifiedByName = "mapUserEntityToId")
    @Mapping(target = "edited", source = "editedAt")
    CommentResponse mapEntityToResponse(CommentEntity commentEntityFromStorage);

    @Named("mapEntityId")
    default EventEntity mapEntityId(Long eventId) {
        if (eventId == null) {
            return null;
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(eventId);
        return eventEntity;
    }

    @Named("mapEventEntityToId")
    default Long mapEventEntityToId(EventEntity eventEntity) {
        return eventEntity.getId();
    }

    @Named("mapUserEntityToId")
    default Long mapUserEntityToId(UserEntity userEntity) {
        return userEntity.getId();
    }
}
