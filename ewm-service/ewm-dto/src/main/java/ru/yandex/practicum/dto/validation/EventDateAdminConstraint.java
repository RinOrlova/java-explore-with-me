package ru.yandex.practicum.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateAdminValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventDateAdminConstraint {
    String message() default "Event date must be at least 1 hour from now.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


