package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record MedicationEntryDTO(
        @DecimalMin(value = "0", message = "Medication value should be greater than or equal 0")
        @DecimalMax(value = "1000", message = "Medication value should be lesser than or equal 1000")
        float value,
        @NotNull(message = "Timestamp of medication taking must be provided")
        Instant commitedAt,
        @NotNull(message = "Name of medication must be provided")
        @Length(max = 100, message = "Name of medication should be less than 100 characters long")
        String name,
        @Length(max = 500, message = "Notes should be less than 500 characters long")
        String notes) {
}
