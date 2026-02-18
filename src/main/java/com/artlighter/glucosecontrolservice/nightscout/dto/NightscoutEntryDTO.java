package com.artlighter.glucosecontrolservice.nightscout.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record NightscoutEntryDTO(
        String type,
        @NotBlank
        String dateString,
        Long date,
        @DecimalMin("10")
        @DecimalMax("720")
        Float sgv
) {
}
