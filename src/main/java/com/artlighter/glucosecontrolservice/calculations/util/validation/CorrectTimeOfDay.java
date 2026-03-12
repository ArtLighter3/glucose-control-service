package com.artlighter.glucosecontrolservice.calculations.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectTimeOfDayValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectTimeOfDay {
    String message() default "time of day for value should be like 00:30, 01:00, 01:30... 23:00, 23:30";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
