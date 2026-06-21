package com.artlighter.glucosecontrolservice.integration.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectGlucoseValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectGlucose {
    String message() default "значение глюкозы должно быть 0.5-40 ммоль/л (9-720 мг/дл)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
