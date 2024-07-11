package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;

import java.util.Collection;

public interface UserStorage {

    UserFull addUser(User user);

    void deleteUser(Long userId);

    Collection<UserFull> getAllUsers(int from, int pageSize);

    Collection<UserFull> getAllUsersByIds(Collection<Long> userIds, int from, int to);


}
