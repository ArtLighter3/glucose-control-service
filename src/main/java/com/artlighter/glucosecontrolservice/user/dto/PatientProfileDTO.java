package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import com.artlighter.glucosecontrolservice.user.util.validation.CorrectApiSettings;
import com.artlighter.glucosecontrolservice.user.util.validation.CorrectGlucoseIntervals;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

@GroupSequence({PatientProfileDTO.class, TypeGroup.class})
@CorrectGlucoseIntervals(groups = {TypeGroup.class})
@CorrectApiSettings(groups = {TypeGroup.class})
public record PatientProfileDTO(
        GlucoseUnit glucoseUnit,
        CarbsUnit carbsUnit,
        @Min(1)
        @Max(2)
        @NotNull
        Integer diabetesType,
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float hyperGlucose,
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float highGlucose,
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float lowGlucose,
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float hypoGlucose,
        @NotNull
        Boolean isNightscoutEnabled,
        String nightscoutApiSecret
) {
}
