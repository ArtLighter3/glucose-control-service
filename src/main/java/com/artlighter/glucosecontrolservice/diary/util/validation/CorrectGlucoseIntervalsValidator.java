package com.artlighter.glucosecontrolservice.diary.util.validation;

import com.artlighter.glucosecontrolservice.diary.dto.PatientProfileDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CorrectGlucoseIntervalsValidator
        implements ConstraintValidator<CorrectGlucoseIntervals, PatientProfileDTO> {
    @Override
    public void initialize(CorrectGlucoseIntervals constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PatientProfileDTO value, ConstraintValidatorContext context) {
        Float high = value.highGlucose(), low = value.lowGlucose(),
                hyper = value.hyperGlucose(), hypo = value.hypoGlucose();

        if (high == null || low == null || hyper == null || hypo == null) return false;

        if (hyper < high) return false;
        if (high < low) return false;
        if (low < hypo) return false;

        return true;
    }
}
