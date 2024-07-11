package ru.yandex.practicum.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFull extends User {
    private Long id;
}
