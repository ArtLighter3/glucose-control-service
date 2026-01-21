package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record GlucoseEntryDTO(
        @DecimalMin(value = "0.5", message = "Glucose value should be greater than or equal 0.5")
        @DecimalMax(value = "40.0", message = "Glucose value should be lesser than or equal 40.0")
        @NotNull
        Float value,
        @NotNull(message = "Timestamp of measurement must be provided")
        Instant commitedAt,
        MeasurementType type,
        @Length(max = 500, message = "Notes should be less than 500 characters long")
        String notes) {
}
