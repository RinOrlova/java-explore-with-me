package ru.yandex.practicum.dto.validation;

import ru.yandex.practicum.exceptions.ForbiddenException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDateConstraint, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull will handle null case
        }

        LocalDateTime twoHoursFromNow = LocalDateTime.now().plusHours(2);
        if (!value.isAfter(twoHoursFromNow)) {
            throw new ForbiddenException("The event can only be changed 2 hours in advance or earlier");
        }
        return true;
    }
}
