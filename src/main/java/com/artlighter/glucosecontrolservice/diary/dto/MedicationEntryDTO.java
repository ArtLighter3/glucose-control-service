package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.PortionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

@Schema(name = "MedicationEntry", description = "Запись приема препарата")
public record MedicationEntryDTO(
        @Schema(description = "Значение принятых порций препарата",
                example = "5")
        @DecimalMin("0.1")
        @DecimalMax("1000")
        @NotNull
        Float value,
        @Schema(description = "Временная отметка приема препарата в формате ISO 8601")
        @NotNull
        OffsetDateTime commitedAt,
        @Schema(description = "Наименование препарата")
        @NotBlank
        @Length(max = 255)
        String name,
        @Schema(description = "Тип порций препарата (таблетки, капли, ст. ложки и т.д.)")
        @NotNull
        PortionType portionType,
        @Schema(description = "Сколько миллиграмм вещества содержится в порции")
        @DecimalMin("0.1")
        @DecimalMax("1000")
        Float milligramsInPortion,
        @Schema(description = "Комментарий-заметка к записи")
        @Length(max = 500)
        String notes)
        implements DiaryEntryDTO {
}
