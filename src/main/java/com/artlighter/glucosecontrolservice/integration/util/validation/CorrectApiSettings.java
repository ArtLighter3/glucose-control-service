package com.artlighter.glucosecontrolservice.integration.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectApiSettingsValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectApiSettings {
    String message() default "некоторые из интеграций включены, но для них не предоставлены настройки";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
