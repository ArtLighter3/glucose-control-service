package com.artlighter.glucosecontrolservice.calculations.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectCalculationRequestValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectCalculationRequest {
    String message()
            default "расчет настроен для понижения уровня глюкозы, но не было предоставлено текущего уровня глюкозы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
