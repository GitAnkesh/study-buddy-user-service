package com.ankesh.studybuddy.user_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default """
            Password must contain at least:
            1 uppercase letter,
            1 lowercase letter,
            1 number,
            1 special character
            """;

    Class<?>[] groups() default {

    };

    Class<? extends Payload>[] payload() default {};
}
