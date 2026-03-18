package com.artlighter.glucosecontrolservice.templates.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Результат подсчета общей дозировки препаратов")
public record MedicationResult(
        @Schema(description = "Общая дозировка препаратов в миллиграммах")
        Float overallDose
) {
}
