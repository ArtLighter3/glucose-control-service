package com.artlighter.glucosecontrolservice.calculations.util.validation;

import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectCalculationRequestValidator
        implements ConstraintValidator<CorrectCalculationRequest, InsulinCalculationRequestDTO> {
    @Override
    public void initialize(CorrectCalculationRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(InsulinCalculationRequestDTO value, ConstraintValidatorContext context) {
        return !value.correctGlucoseLevel() || value.glucose() != null;
    }
}
