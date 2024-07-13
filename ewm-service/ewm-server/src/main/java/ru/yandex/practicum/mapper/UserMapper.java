package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.storage.user.UserEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity mapUserToUserEntity(User user);

    @Named("mapUserIdToUserEntity")
    default UserEntity mapUserIdToUserEntity(Long userId) {
        if (userId == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        return userEntity;
    }

    @Named("mapUserEntityToUser")
    User mapUserEntityToUser(UserEntity userEntity);

    @Named("mapUserEntityToUserFull")
    UserFull mapUserEntityToUserFull(UserEntity userEntity);


}
