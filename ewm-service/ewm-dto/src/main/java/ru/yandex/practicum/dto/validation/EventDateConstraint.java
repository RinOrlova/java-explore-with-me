package ru.yandex.practicum.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateConstraint {
    String message() default "Event date must be at least 2 hours from now.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
