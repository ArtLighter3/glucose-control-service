package com.artlighter.glucosecontrolservice.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Schema(name = "CarbsEntry", description = "Запись с принятыми углеводами")
public record CarbsEntryDTO(
        @Schema(description = "Значение углеводов в тех единицах измерения, которые указаны в профиле больного",
                example = "25.5")
        @DecimalMin("0")
        @DecimalMax("300")
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
