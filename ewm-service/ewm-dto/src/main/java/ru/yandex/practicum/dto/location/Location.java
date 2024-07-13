package ru.yandex.practicum.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @JsonIgnore
    Long id;
    Double lat;
    Double lon;

}
