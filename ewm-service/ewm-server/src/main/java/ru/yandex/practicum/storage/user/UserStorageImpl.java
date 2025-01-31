package ru.yandex.practicum.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.exceptions.ConflictException;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.mapper.UserMapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStorageImpl implements UserStorage {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserFull addUser(User user) {
        Optional<UserEntity> userByEmail = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));
        if (userByEmail.isPresent()) {
            throw new ConflictException("User with this email is already registered.");
        }
        UserEntity userEntity = userMapper.mapUserToUserEntity(user);
        UserEntity userFromStorage = userRepository.saveAndFlush(userEntity);
        return userMapper.mapUserEntityToUserFull(userFromStorage);
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException exception) {
            log.warn("User not found by requested id:{}", userId);
            throw new EntityNotFoundException(userId, UserEntity.class);
        }
    }

    @Override
    public Collection<UserFull> getAllUsers(int from, int pageSize) {
        PageRequest pageRequest = PageRequest.of(from, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserEntity> userEntityPage = userRepository.findAll(pageRequest);
        return mapUserEntitiesToUser(userEntityPage);
    }

    @Override
    public Collection<UserFull> getAllUsersByIds(Collection<Long> userIds, int from, int pageSize) {
        PageRequest pageRequest = PageRequest.of(from, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserEntity> userEntityPage = userRepository.findAllByUserIds(userIds, pageRequest);
        return mapUserEntitiesToUser(userEntityPage);
    }

    @Override
    public UserFull getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::mapUserEntityToUserFull)
                .orElseThrow(() -> new EntityNotFoundException(id, UserEntity.class));
    }

    private Collection<UserFull> mapUserEntitiesToUser(Page<UserEntity> userEntityPage) {
        return userEntityPage.stream().map(userMapper::mapUserEntityToUserFull).collect(Collectors.toList());
    }
}
