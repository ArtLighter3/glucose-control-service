package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Schema(name = "GlucoseEntry", description = "Запись с измерением глюкозы")
public record GlucoseEntryDTO(
        @Schema(description = "Значение глюкозы в тех единицах измерения, которые указаны в профиле больного. " +
                "Допустимый диапазон также зависит от единицы измерения: 0.5-40 ммоль/л, 9-720 мг/дл",
                example = "9.6")
        @NotNull
        Float value,
        @Schema(description = "Временная отметка измерения глюкозы в формате ISO 8601")
        @NotNull
        OffsetDateTime commitedAt,
        @Schema(description = "Тип измерения (до еды, после еды)", example = "AFTER_MEAL")
        MeasurementType type,
        @Schema(description = "Комментарий-заметка к записи")
        @Length(max = 500)
        String notes,
        @Schema(description = "Единица измерения глюкозы. Используется только на выходе", example = "MMOL_PER_LITERS")
        String glucoseUnits)

        implements DiaryEntryDTO {
}
