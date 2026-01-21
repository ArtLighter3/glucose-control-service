package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record InsulinEntryDTO(
        @DecimalMin(value = "1", message = "Insulin value should be greater than or equal 1")
        @DecimalMax(value = "100", message = "Insulin value should be lesser than or equal 100")
        @NotNull
        Float value,
        @NotNull(message = "Timestamp of insulin administration must be provided")
        Instant commitedAt,
        @NotNull(message = "Insulin type must be provided")
        InsulinType type,
        @Length(max = 500, message = "Notes should be less than 500 characters long")
        String notes) {
}
