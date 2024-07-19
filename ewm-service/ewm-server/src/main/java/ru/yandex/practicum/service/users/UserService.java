package ru.yandex.practicum.service.users;

import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;

import java.util.Collection;
import java.util.Set;

public interface UserService {

    UserFull addUser(User user);

    void deleteUser(Long userId);

    Collection<UserFull> getAllUsers(Set<Long> userIds, int from, int to);


}
