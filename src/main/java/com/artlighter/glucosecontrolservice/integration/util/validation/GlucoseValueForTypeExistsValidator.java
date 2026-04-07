package com.artlighter.glucosecontrolservice.integration.util.validation;

import com.artlighter.glucosecontrolservice.integration.dto.NightscoutEntryDTO;
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
