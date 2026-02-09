package com.artlighter.glucosecontrolservice.templates.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record MedicationDTO(
        @NotBlank
        @Length(max = 200)
        String name,
        @NotNull
        @DecimalMin("0.1")
        @DecimalMax("1000")
        Float milligramsInPortion,
        @Min(1)
        @Max(20)
        Integer defaultPortions
) implements PatientTemplateEntityDTO {
}
