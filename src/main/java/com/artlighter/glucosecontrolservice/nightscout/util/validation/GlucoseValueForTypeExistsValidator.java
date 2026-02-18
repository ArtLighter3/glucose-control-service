package com.artlighter.glucosecontrolservice.nightscout.util.validation;

import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutEntryDTO;
import com.artlighter.glucosecontrolservice.nightscout.dto.NightscoutTreatmentDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GlucoseValueForTypeExistsValidator
        implements ConstraintValidator<GlucoseValueForTypeExists, NightscoutEntryDTO> {
    @Override
    public void initialize(GlucoseValueForTypeExists constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(NightscoutEntryDTO value, ConstraintValidatorContext context) {
        if (value.type().equals("sgv")) return value.sgv() != null;
        else if (value.type().equals("mbg")) return value.mbg() != null;
        else return value.cal() != null;
    }
}
