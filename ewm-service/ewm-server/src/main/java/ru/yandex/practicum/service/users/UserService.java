package ru.yandex.practicum.service.users;

import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;

import java.util.Collection;

public interface UserService {

    UserFull addUser(User user);

    void deleteUser(Long userId);

    Collection<UserFull> getAllUsers(Collection<Long> userIds, int from, int to);


}
