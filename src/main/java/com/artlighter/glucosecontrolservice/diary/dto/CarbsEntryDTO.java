package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

public record CarbsEntryDTO(
        @DecimalMin(value = "0", message = "Carbohydrates value should be greater than or equal 0")
        @DecimalMax(value = "300", message = "Carbohydrates value should be lesser than or equal 300")
        @NotNull
        Float value,
        @NotNull(message = "Timestamp of meal must be provided")
        Instant commitedAt,
        @Length(max = 500, message = "Notes should be less than 500 characters long")
        String notes) {
}
