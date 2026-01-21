package com.artlighter.glucosecontrolservice.diary.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectGlucoseIntervalsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectGlucoseIntervals {
    String message() default "some of the target glucose value is out of bounds " +
            "relative to its higher or lower target glucose";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
