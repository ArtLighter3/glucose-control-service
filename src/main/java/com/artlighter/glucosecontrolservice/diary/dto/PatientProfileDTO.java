package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import com.artlighter.glucosecontrolservice.diary.util.validation.ArgumentsGroup;
import com.artlighter.glucosecontrolservice.diary.util.validation.CorrectGlucoseIntervals;
import com.artlighter.glucosecontrolservice.diary.util.validation.TypeGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

//@GroupSequence({ArgumentsGroup.class, TypeGroup.class})
@CorrectGlucoseIntervals(groups = {TypeGroup.class})
public record PatientProfileDTO(
        GlucoseUnit glucoseUnit,
        CarbsUnit carbsUnit,
        @Min(value = 1, groups = {ArgumentsGroup.class})
        @Max(value = 2, groups = {ArgumentsGroup.class})
        int diabetesType,
        @DecimalMin(value = "1", groups = {ArgumentsGroup.class})
        @DecimalMax(value = "40", groups = {ArgumentsGroup.class})
        @NotNull
        Float hyperGlucose,
        @DecimalMin(value = "1", groups = {ArgumentsGroup.class})
        @DecimalMax(value = "40", groups = {ArgumentsGroup.class})
        @NotNull
        Float highGlucose,
        @DecimalMin(value = "1", groups = {ArgumentsGroup.class})
        @DecimalMax(value = "40", groups = {ArgumentsGroup.class})
        @NotNull
        Float lowGlucose,
        @DecimalMin(value = "1", groups = {ArgumentsGroup.class})
        @DecimalMax(value = "40", groups = {ArgumentsGroup.class})
        @NotNull
        Float hypoGlucose
) {
}
