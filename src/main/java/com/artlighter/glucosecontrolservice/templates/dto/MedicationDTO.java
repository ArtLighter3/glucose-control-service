package com.artlighter.glucosecontrolservice.templates.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.PortionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Schema(name = "Medication", description = "Сохраненный пользователем препарат")
public record MedicationDTO(
        @Schema(description = "Имя препарата")
        @NotBlank
        @Length(max = 255)
        String name,
        @Schema(description = "Сколько миллиграмм вещества содержится в порции.")
        @DecimalMin("0.1")
        @DecimalMax("1000")
        Float milligramsInPortion,
        @Schema(description = "Количество порций по-умолчанию. Нужно для того, чтобы каждый раз не заполнять кол-во" +
                " порций при подсчете, если оно почти всегда одинаковое для препарата у пользователя")
        @DecimalMin("0.1")
        @DecimalMax("1000")
        Float defaultPortions,
        @Schema(description = "Тип порций препарата (таблетки, капли, ст. ложки и т.д.)")
        @NotNull
        PortionType portionType
) implements PatientTemplateEntityDTO {
}
