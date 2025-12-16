package com.artlighter.glucosecontrolservice.auth.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserDoesNotExistValidator.class)
public @interface UserDoesNotExist {
    String message() default "user with this username already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
