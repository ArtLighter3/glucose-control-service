package com.artlighter.glucosecontrolservice.integration.util.validation;

import com.artlighter.glucosecontrolservice.integration.dto.NightscoutTreatmentDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectGlucoseValidator
        implements ConstraintValidator<CorrectGlucose, NightscoutTreatmentDTO> {
    @Override
    public void initialize(CorrectGlucose constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(NightscoutTreatmentDTO value, ConstraintValidatorContext context) {
        if (value.glucose() == null || value.glucose() <= 0f) return true;

        if (value.units() == null) return false;
        if (value.units().equals("mg/dl") && (value.glucose() < 10f || value.glucose() > 720f))
            return false;
        return !value.units().equals("mmol") || (!(value.glucose() < 0.5f) && value.glucose() <= 40f);
    }
}
