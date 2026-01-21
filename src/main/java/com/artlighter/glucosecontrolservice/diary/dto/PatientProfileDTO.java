package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import com.artlighter.glucosecontrolservice.diary.util.validation.ArgumentsGroup;
import com.artlighter.glucosecontrolservice.diary.util.validation.CorrectGlucoseIntervals;
import com.artlighter.glucosecontrolservice.diary.util.validation.TypeGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

@GroupSequence({PatientProfileDTO.class, TypeGroup.class})
@CorrectGlucoseIntervals(groups = {TypeGroup.class})
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
        Float hypoGlucose
) {
}
