package com.artlighter.glucosecontrolservice.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Schema(name = "CarbsEntry", description = "Запись с принятыми углеводами")
public record CarbsEntryDTO(
        @Schema(description = "Значение углеводов в тех единицах измерения, которые указаны в профиле больного. " +
                "Допустимый диапазон также зависит от единиц измерения: 0.1-300 грамм, 0.01-30 ХЕ(10), 0.01-25 ХЕ(12), " +
                "0.01-20 ХЕ(15)", example = "25.5")
        @NotNull
        Float value,
        @Schema(description = "Временная отметка принятия углеводов в формате ISO 8601")
        @NotNull
        OffsetDateTime commitedAt,
        @Schema(description = "Комментарий-заметка к записи")
        @Length(max = 500)
        String notes,
        @Schema(description = "Единица измерения углеводов. Используется только на выходе", example = "GRAMS")
        String carbsUnits)

        implements DiaryEntryDTO {
}
