package com.artlighter.glucosecontrolservice.auth.util.validation;

import com.artlighter.glucosecontrolservice.user.dto.UserRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDTO> {
    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationDTO value, ConstraintValidatorContext context) {
        return value.password().equals(value.repeatedPassword());
    }
}
