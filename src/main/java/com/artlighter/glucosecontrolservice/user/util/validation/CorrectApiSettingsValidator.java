package com.artlighter.glucosecontrolservice.user.util.validation;

import com.artlighter.glucosecontrolservice.user.dto.PatientProfileDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectApiSettingsValidator
        implements ConstraintValidator<CorrectApiSettings, PatientProfileDTO> {
    @Override
    public void initialize(CorrectApiSettings constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PatientProfileDTO value, ConstraintValidatorContext context) {
        if (value.isNightscoutEnabled() == null) return true;

        return value.nightscoutApiSecret() != null && !value.nightscoutApiSecret().isBlank();
    }
}
