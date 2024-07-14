package ru.yandex.practicum.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;
    @NotBlank
    @Size(max = 50)
    @JsonProperty("name")
    private String name;
}
