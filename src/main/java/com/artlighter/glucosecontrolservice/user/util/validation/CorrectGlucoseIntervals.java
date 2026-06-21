package com.artlighter.glucosecontrolservice.user.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectGlucoseIntervalsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectGlucoseIntervals {
    String message() default "какие-то из пределов интервалов глюкозы выходят за рамки " +
            "более высокого или низкого интервала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
