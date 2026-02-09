package com.artlighter.glucosecontrolservice.templates.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MealDTO(
        @NotBlank
        @Length(max = 200)
        String name,
        @NotNull
        @DecimalMin("0")
        @DecimalMax("300")
        Float carbsPer100Grams
) implements PatientTemplateEntityDTO {
}
