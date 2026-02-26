package com.artlighter.glucosecontrolservice.calculations.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.Map;

@Schema(name = "InsulinProfile", description = "Инсулиновый профиль для расчетов инсулина")
public record InsulinProfileDTO(
        @Schema(description = "Соотношение инсулина к углеводам (ICR) по-умолчанию. Будет использоваться, " +
                "если не было найдено значения ICR для конкретного времени суток")
        @DecimalMin(value = "2")
        @DecimalMax(value = "100")
        @NotNull
        Float defaultInsulinToCarbsRatio,
        @Schema(description = "Фактор чувствительности к инсулину (ISF) по-умолчанию. Будет использоваться, " +
                "если не было найдено значения ISF для конкретного времени суток")
        @DecimalMin(value = "0.2")
        @DecimalMax(value = "55.5")
        @NotNull
        Float defaultInsulinSensitivityFactor,
        @Schema(description = "Длительность действия короткого инсулина")
        @Min(value = 2)
        @Max(value = 9)
        int durationOfInsulinAction,
        @Schema(description = "Значения ISF по времени суток")
        Map<@NotNull LocalTime,
                @DecimalMin(value = "0.2")
                @DecimalMax(value = "55.5")
                        Float> factorsByTime,
        @Schema(description = "Значения ICR по времени суток")
        Map<@NotNull LocalTime,
                @DecimalMin(value = "2")
                @DecimalMax(value = "100")
                        Float> ratiosByTime
) {
}
