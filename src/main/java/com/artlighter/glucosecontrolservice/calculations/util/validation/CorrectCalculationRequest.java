package com.artlighter.glucosecontrolservice.calculations.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectCalculationRequestValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectCalculationRequest {
    String message() default "calculation is requested to correct glucose level but no current glucose was provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
