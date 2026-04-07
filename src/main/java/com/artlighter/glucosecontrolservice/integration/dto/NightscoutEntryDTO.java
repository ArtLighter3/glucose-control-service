package com.artlighter.glucosecontrolservice.integration.dto;

import com.artlighter.glucosecontrolservice.general.TypeGroup;
import com.artlighter.glucosecontrolservice.integration.util.validation.GlucoseValueForTypeExists;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

@GroupSequence({NightscoutEntryDTO.class, TypeGroup.class})
@GlucoseValueForTypeExists(groups = TypeGroup.class)
public record NightscoutEntryDTO(
        @NotBlank
        @Pattern(regexp = "sgv|mbg|cal")
        String type,
        String dateString,
        @NotNull
        Long date,
        @DecimalMin("10")
        @DecimalMax("720")
        Float sgv,
        @DecimalMin("10")
        @DecimalMax("720")
        Float mbg,
        @DecimalMin("10")
        @DecimalMax("720")
        Float cal
) {
}
