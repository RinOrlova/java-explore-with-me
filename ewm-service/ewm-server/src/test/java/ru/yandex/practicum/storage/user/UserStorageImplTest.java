package ru.yandex.practicum.storage.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.dto.user.User;
import ru.yandex.practicum.dto.user.UserFull;
import ru.yandex.practicum.exceptions.UserNotFoundException;
import ru.yandex.practicum.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaRepositories(basePackages = "ru.yandex.practicum.storage.user")
@EntityScan(basePackages = "ru.yandex.practicum.storage.user")
public class UserStorageImplTest {

    private UserStorageImpl userStorage;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userStorage = new UserStorageImpl(userRepository, userMapper);
    }

    @Test
    public void testAddUser_defaultUser() {
        User user = new User();
        user.setName("Name");
        user.setEmail("email@domain.com");

        UserFull userFull = userStorage.addUser(user);

        assertThat(userFull).isNotNull();
        assertThat(userFull.getName()).isEqualTo("Name");
        assertThat(userFull.getEmail()).isEqualTo("email@domain.com");

        Optional<UserEntity> savedEntity = userRepository.findById(userFull.getId());
        assertTrue(savedEntity.isPresent());
        UserEntity userEntity = savedEntity.get();
        assertEquals("Name", userEntity.getName());
        assertEquals("email@domain.com", userEntity.getEmail());
    }

    @Test
    public void testDeleteUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName("Name 2");
        userEntity.setEmail("email2@domain.com");
        userEntity = userRepository.saveAndFlush(userEntity);

        userStorage.deleteUser(userEntity.getId());

        assertThat(userRepository.findById(userEntity.getId())).isEmpty();
    }

    @Test
    public void testDeleteNonExistentUser() {
        Long nonExistentUserId = 999L;
        assertThatThrownBy(() -> userStorage.deleteUser(nonExistentUserId))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testGetAllUsers() {
        UserEntity user1 = new UserEntity();
        user1.setName("User One");
        user1.setEmail("user.one@domain.com");

        UserEntity user2 = new UserEntity();
        user2.setName("User Two");
        user2.setEmail("user.two@domain.com");

        userRepository.saveAll(List.of(user1, user2));

        Collection<UserFull> users = userStorage.getAllUsers(0, 10);

        assertEquals(2, users.size());
        assertThat(users).extracting("name").containsExactlyInAnyOrder("User One", "User Two");
    }

    @Test
    public void testGetAllUsersByIds() {
        UserEntity user1 = new UserEntity();
        user1.setName("User One");
        user1.setEmail("user.one@domain.com");

        UserEntity user2 = new UserEntity();
        user2.setName("User Two");
        user2.setEmail("user.two@domain.com");

        userRepository.saveAll(List.of(user1, user2));

        List<Long> userIds = List.of(user1.getId(), user2.getId());

        Collection<UserFull> users = userStorage.getAllUsersByIds(userIds, 0, 10);

        assertEquals(2, users.size());
        assertThat(users).extracting("name").containsExactlyInAnyOrder("User One", "User Two");
    }

    @Test
    public void testGetAllUsersByIds_smallPage() {
        UserEntity user1 = new UserEntity();
        user1.setName("User One");
        user1.setEmail("user.one@domain.com");

        UserEntity user2 = new UserEntity();
        user2.setName("User Two");
        user2.setEmail("user.two@domain.com");

        userRepository.saveAll(List.of(user1, user2));

        List<Long> userIds = List.of(user1.getId(), user2.getId());

        Collection<UserFull> users = userStorage.getAllUsersByIds(userIds, 0, 1);

        assertEquals(1, users.size());
        assertThat(users).extracting("name").containsExactlyInAnyOrder("User One");
    }
}
