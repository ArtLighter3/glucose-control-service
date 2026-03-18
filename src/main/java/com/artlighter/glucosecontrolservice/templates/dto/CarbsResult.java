package com.artlighter.glucosecontrolservice.templates.dto;

import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Результат расчетов общего кол-ва углеводов")
public record CarbsResult(
        @Schema(description = "Значение рассчитанных углеводов", example = "45.5")
        Float overallCarbs,
        @Schema(description = "Единицы измерения углеводов")
        CarbsUnit carbsUnit
) {
}
