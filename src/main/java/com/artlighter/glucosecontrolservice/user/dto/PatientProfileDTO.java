package com.artlighter.glucosecontrolservice.user.dto;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import com.artlighter.glucosecontrolservice.user.util.validation.CorrectGlucoseIntervals;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Schema(name = "PatientProfile", description = "Профиль больного с его настройками")
@GroupSequence({PatientProfileDTO.class, TypeGroup.class})
@CorrectGlucoseIntervals(groups = {TypeGroup.class})
public record PatientProfileDTO(
        @Schema(description = "Единицы измерения глюкозы при ведении дневника")
        GlucoseUnit glucoseUnit,
        @Schema(description = "Единицы измерения углеводов при ведении дневника")
        CarbsUnit carbsUnit,
        @Schema(description = "Тип диабета")
        @Min(1)
        @Max(2)
        @NotNull
        Integer diabetesType,
        @Schema(description = "Предельно высокое значение глюкозы (в единицах измерения, выставленных пользователем " +
                "в этом же объекте)")
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float hyperGlucose,
        @Schema(description = "Верхняя граница нормы глюкозы (в единицах измерения, выставленных пользователем " +
                "в этом же объекте)")
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float highGlucose,
        @Schema(description = "Нижняя граница нормы глюкозы (в единицах измерения, выставленных пользователем " +
                "в этом же объекте)")
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float lowGlucose,
        @Schema(description = "Предельно низкое значение глюкозы (в единицах измерения, выставленных пользователем " +
                "в этом же объекте)")
        @DecimalMin("1")
        @DecimalMax("40")
        @NotNull
        Float hypoGlucose
) {
}
