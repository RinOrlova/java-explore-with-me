package ru.yandex.practicum.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserFull addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public Collection<UserFull> getAllUsers(@Nullable Set<Long> userIds, int from, int pageSize) {
        if (userIds == null || userIds.isEmpty()) {
            return userStorage.getAllUsers(from, pageSize);
        }
        return userStorage.getAllUsersByIds(userIds, from, pageSize);
    }
}
