package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Schema(name = "InsulinEntry", description = "Запись ввода инсулина")
public record InsulinEntryDTO(
        @Schema(description = "Значение принятого инсулина в единицах",
                example = "1.5")
        @DecimalMin("1")
        @DecimalMax("100")
        @NotNull
        Float value,
        @Schema(description = "Временная отметка ввода инсулина в формате ISO 8601")
        @NotNull
        OffsetDateTime commitedAt,
        @Schema(description = "Тип инсулина (короткого, длительного действия и т.д.)",
                example = "LONG")
        @NotNull
        InsulinType type,
        @Schema(description = "Комментарий-заметка к записи")
        @Length(max = 500)
        String notes)

        implements DiaryEntryDTO {
}
