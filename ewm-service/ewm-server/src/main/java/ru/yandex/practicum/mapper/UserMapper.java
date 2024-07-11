package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.storage.user.UserEntity;

@Mapper(componentModel = org.mapstruct.MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity mapUserToUserEntity(User user);

    User mapUserEntityToUser(UserEntity userEntity);

    UserFull mapUserEntityToUserFull(UserEntity userEntity);


}
