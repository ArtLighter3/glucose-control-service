package com.artlighter.glucosecontrolservice.calculations.dto;

import com.artlighter.glucosecontrolservice.calculations.util.validation.CorrectTimeOfDay;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.Map;

@Schema(name = "InsulinProfile", description = "Инсулиновый профиль для расчетов инсулина")
public record InsulinProfileDTO(
        @Schema(description = "Соотношение инсулина к углеводам (ICR) по-умолчанию. Будет использоваться, " +
                "если не было найдено значения ICR для конкретного времени суток. Углеводы только в граммах.")
        @DecimalMin(value = "2")
        @DecimalMax(value = "100")
        @NotNull
        Float defaultInsulinToCarbsRatio,
        @Schema(description = "Фактор чувствительности к инсулину (ISF) по-умолчанию. Будет использоваться, " +
                "если не было найдено значения ISF для конкретного времени суток. ISF может быть задан как в " +
                "ммоль/л (0.2 - 55.5), так и в мг/дл (3.6 - 999) в зависимости от единиц пользователя.")
        @NotNull
        Float defaultInsulinSensitivityFactor,
        @Schema(description = "Длительность действия короткого инсулина (часы)")
        @Min(value = 2)
        @Max(value = 9)
        int durationOfInsulinAction,
        @Schema(description = "Значения ISF по времени суток. Принимаются значения, " +
                "начиная от 00:30 с интервалом по 30 минут.")
        Map<@CorrectTimeOfDay LocalTime, Float> factorsByTime,
        @Schema(description = "Значения ISF по времени суток. Принимаются значения, " +
                "начиная от 00:30 с интервалом по 30 минут.")
        Map<@CorrectTimeOfDay LocalTime,
                @DecimalMin(value = "2")
                @DecimalMax(value = "100")
                        Float> ratiosByTime
) {
}
