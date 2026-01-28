package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.calculations.util.validation.CorrectCalculationRequest;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.time.LocalTime;

@GroupSequence({InsulinCalculationRequestDTO.class, TypeGroup.class})
@CorrectCalculationRequest(groups = {TypeGroup.class})
public record InsulinCalculationRequestDTO(
        @NotNull
        @DecimalMin("0")
        @DecimalMax("300")
        Float carbs,
        @DecimalMin("0.5")
        @DecimalMax("40")
        Float glucose,
        @NotNull
        LocalTime localTimeOfDay,
        @NotNull
        Boolean considerActiveInsulin,
        @NotNull
        Boolean correctGlucoseLevel,
        Float correction
) {
}
