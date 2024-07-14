package ru.yandex.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Size(min = 2, max = 250)
    @NotBlank
    private String name;
    @Size(min = 6, max = 254)
    @Email
    @NotBlank
    private String email;

}
