package com.artlighter.glucosecontrolservice.calculations.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

/**
 * Валидатор, проверяющий то, что переданное время суток для значения инсулинового профиля,
 * зависящего от времени суток, является корректным временным слотом (такие значения можно задавать для каждых
 * 30 минут времени суток, начиная от 00:30).
 */

public class CorrectTimeOfDayValidator
        implements ConstraintValidator<CorrectTimeOfDay, LocalTime> {
    @Override
    public void initialize(CorrectTimeOfDay constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        if (value == null) return false;

        LocalTime currentValidTimeSlot = LocalTime.of(0, 30);

        for (int i = 0; i < 48; i++) {
            if (value.equals(currentValidTimeSlot)) return true;
            currentValidTimeSlot = currentValidTimeSlot.plusMinutes(30);
        }

        return false;
    }
}
