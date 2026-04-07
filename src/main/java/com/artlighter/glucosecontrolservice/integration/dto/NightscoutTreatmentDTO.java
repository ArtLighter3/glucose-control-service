package com.artlighter.glucosecontrolservice.integration.dto;

import com.artlighter.glucosecontrolservice.general.TypeGroup;
import com.artlighter.glucosecontrolservice.integration.util.validation.CorrectGlucose;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@GroupSequence({NightscoutTreatmentDTO.class, TypeGroup.class})
@CorrectGlucose(groups = TypeGroup.class)
public record NightscoutTreatmentDTO(
        @NotBlank
        @JsonProperty("created_at")
        String createdAt,
        String eventType,
        Float glucose,
        @DecimalMin("0")
        @DecimalMax("300")
        Float carbs,
        @DecimalMin("1")
        @DecimalMax("100")
        Float insulin,
        @Pattern(regexp = "mg/dl|mmol")
        String units,
        @Length(max = 500)
        String notes
) {
}
