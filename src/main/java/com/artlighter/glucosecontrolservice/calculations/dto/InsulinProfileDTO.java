package com.artlighter.glucosecontrolservice.calculations.dto;

import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.Map;

public record InsulinProfileDTO(
        @DecimalMin(value = "2", message = "default ICR should be greater than or equal 2")
        @DecimalMax(value = "100", message = "default ICR should be lesser than or equal 100")
        @NotNull
        Float defaultInsulinToCarbsRatio,
        @DecimalMin(value = "0.2", message = "default ISF should be greater than or equal 0.2")
        @DecimalMax(value = "55.5", message = "default ISF should be lesser than or equal 55.5")
        @NotNull
        Float defaultInsulinSensitivityFactor,
        @Min(value = 2, message = "DIA should be greater than or equal 2")
        @Max(value = 9, message = "DIA should be lesser than or equal 9")
        int durationOfInsulinAction,
        Map<@NotNull(message = "time must be provided") LocalTime,
                @DecimalMin(value = "0.2", message = "ISF should be greater than or equal 0.2")
                @DecimalMax(value = "55.5", message = "ISF should be lesser than or equal 55.5")
                        Float> factorsByTime,
        Map<@NotNull(message = "time must be provided") LocalTime,
                @DecimalMin(value = "2", message = "ICR should be greater than or equal 2")
                @DecimalMax(value = "100", message = "ICR should be lesser than or equal 100")
                        Float> ratiosByTime
) {
}
