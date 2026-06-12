package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.calculations.util.validation.CorrectCalculationRequest;
import com.artlighter.glucosecontrolservice.general.TypeGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;

import java.time.ZoneOffset;

@Schema(name = "InsulinCalculationRequest",
        description = "Запрос на расчет инсулина с нужными параметрами и настройками расчета")
@GroupSequence({InsulinCalculationRequestDTO.class, TypeGroup.class})
@CorrectCalculationRequest(groups = {TypeGroup.class})
public record InsulinCalculationRequestDTO(
        @Schema(description = "Количество углеводов для расчета компенсации (в единицах измерения, выставленных " +
                "пользователем, диапазоны: 1-300 грамм, 0.01-30 ХЕ(10), 0.01-25 ХЕ(12), 0.01-20 ХЕ(15) )",
                example = "60.5")
        @NotNull
        Float carbs,
        @Schema(description = "Уровень глюкозы для расчета корректировки до целевого значения " +
                "(в единицах измерения, выставленных пользователем, диапазоны: 0.5-40 ммоль/л, 9-720 мг/дл). " +
                "Должно быть указано обязательно, если correctGlucoseLevel = true", example = "9.7")
        Float glucose,
        @Schema(description = "UTC-смещение зоны пользователя. " +
                "Нужно для правильного определения параметров, изменяемых по времени суток " +
                "(ISF, ICR...).",
                example = "+07:30")
        @NotNull
        ZoneOffset patientZoneOffset,
        @Schema(description = "Учитывать ли активный инсулин. Активный инсулин будет рассчитываться " +
                "исходя из недавних записей ввода инсулина.")
        @NotNull
        Boolean considerActiveInsulin,
        @Schema(description = "Рассчитывать ли дополнительную дозу для корректировки " +
                "уровня глюкозы до целевого значения. Целевое значение определяется как уровень высокой глюкозы по " +
                "настройкам в профиле больного.")
        @NotNull
        Boolean correctGlucoseLevel,
        @Schema(description = "Значение коррекции в процентах для корректировки " +
                "(увеличение, уменьшение) конечного результата", example = "-50")
        @DecimalMin("-100")
        @DecimalMax("100")
        Float correction
) {
}
