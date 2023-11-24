package com.clodrock.sakabe.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PropertyValidator.class)
public @interface ValidProperty {
    String message() default "Invalid property name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
