package com.artlighter.glucosecontrolservice.integration.util.validation;

import com.artlighter.glucosecontrolservice.integration.dto.IntegrationProfileDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectApiSettingsValidator
        implements ConstraintValidator<CorrectApiSettings, IntegrationProfileDTO> {
    @Override
    public void initialize(CorrectApiSettings constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(IntegrationProfileDTO value, ConstraintValidatorContext context) {
        if (value.isNightscoutEnabled() == null || !value.isNightscoutEnabled()) return true;

        return value.nightscoutApiSecret() != null && !value.nightscoutApiSecret().isBlank();
    }
}
