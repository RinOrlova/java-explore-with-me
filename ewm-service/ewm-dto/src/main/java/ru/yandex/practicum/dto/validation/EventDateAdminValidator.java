package ru.yandex.practicum.dto.validation;

import ru.yandex.practicum.exceptions.ForbiddenException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateAdminValidator implements ConstraintValidator<EventDateAdminConstraint, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true; // @NotNull will handle null case
        }

        LocalDateTime twoHoursFromNow = LocalDateTime.now().plusHours(1);
        if (value.isBefore(twoHoursFromNow)) {
            throw new ForbiddenException("The event can only be changed 1 hour in advance or earlier");
        }
        return true;
    }

}
