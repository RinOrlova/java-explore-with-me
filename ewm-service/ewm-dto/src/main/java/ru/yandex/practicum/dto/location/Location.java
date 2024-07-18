package ru.yandex.practicum.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @JsonIgnore
    private Long id;
    @Min(-90)
    @Max(90)
    @NotNull
    private Double lat;
    @Min(-180)
    @Max(180)
    @NotNull
    private Double lon;

}
