package ru.yandex.practicum.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateConfigurableValidator implements ConstraintValidator<EventDateConfigurableConstraint, LocalDateTime> {

    private int hours;

    @Override
    public void initialize(EventDateConfigurableConstraint constraintAnnotation) {
        this.hours = constraintAnnotation.hours();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // @NotNull will handle null case
        }

        LocalDateTime threshold = LocalDateTime.now().plusHours(hours);
        if (value.isBefore(threshold)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
