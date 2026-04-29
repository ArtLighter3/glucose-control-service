package com.artlighter.glucosecontrolservice.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GlucoseDistribution",
        description = "Объект с информацией о распределении уровней глюкозы " +
                "за выбранный период в процентном соотношении")
public record GlucoseDistributionDTO(
        @Schema(description = "Доля нахождения глюкозы в диапазоне выше уровня гипергликемии (от 0 до 1)")
        double aboveHyperGlucose,
        @Schema(description = "Доля нахождения глюкозы в диапазоне ниже уровня гипергликемии, " +
                "но выше целевого диапазона (от 0 до 1)")
        double aboveHighGlucose,
        @Schema(description = "Доля нахождения глюкозы в целевом диапазоне (от 0 до 1)")
        double inTargetRange,
        @Schema(description = "Доля нахождения глюкозы в диапазоне ниже целевого уровня, " +
                "но выше уровня гипогликемии (от 0 до 1)")
        double belowLowGlucose,
        @Schema(description = "Доля нахождения глюкозы в диапазоне ниже уровня гипогликемии (от 0 до 1)")
        double belowHypoGlucose
) {
}
