package com.artlighter.glucosecontrolservice.integration.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectGlucoseValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectGlucose {
    String message() default "glucose value should be in range 0.5-40 mmol/L (10-720 mg/dL) with specified units";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
