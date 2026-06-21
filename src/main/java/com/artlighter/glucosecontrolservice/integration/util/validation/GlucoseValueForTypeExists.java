package com.artlighter.glucosecontrolservice.integration.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GlucoseValueForTypeExistsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlucoseValueForTypeExists {
    String message() default "значение глюкозы должно быть предоставлено (sgv, mbg или cal)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
