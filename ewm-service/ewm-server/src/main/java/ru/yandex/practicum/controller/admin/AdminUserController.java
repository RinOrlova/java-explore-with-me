package ru.yandex.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.service.users.UserService;
import ru.yandex.practicum.utils.ApiPathConstants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPathConstants.ADMIN_PATH + ApiPathConstants.USERS_PATH)
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserFull> getAllUsers(
            @RequestParam(name = "ids", required = false) @Nullable List<Long> userIds,
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return userService.getAllUsers(userIds, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFull addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @DeleteMapping(ApiPathConstants.BY_ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long id) {
        userService.deleteUser(id);
    }
}
