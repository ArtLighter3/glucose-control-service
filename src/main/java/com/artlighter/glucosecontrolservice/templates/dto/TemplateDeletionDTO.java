package com.artlighter.glucosecontrolservice.templates.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TemplateDeletionDTO(
        @NotBlank
        String name
) {
}
