package ru.yandex.practicum.storage.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.id IN :userIds")
    Page<UserEntity> findAllByUserIds(@Param("userIds") Collection<Long> userIds, Pageable pageable);

    UserEntity findByEmail(String email);
}
