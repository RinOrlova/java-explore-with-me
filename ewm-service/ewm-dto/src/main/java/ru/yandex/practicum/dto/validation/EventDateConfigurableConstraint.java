package ru.yandex.practicum.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateConfigurableValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConfigurableConstraint {
    String message() default "Event date must be at least {hours} hours from now.";

    int hours() default 2;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}