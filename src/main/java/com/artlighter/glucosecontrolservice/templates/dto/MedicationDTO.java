package com.artlighter.glucosecontrolservice.templates.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

@Schema(name = "Medication", description = "Сохраненный пользователем препарат")
public record MedicationDTO(
        @Schema(description = "Имя препарата")
        @NotBlank
        @Length(max = 200)
        String name,
        @Schema(description = "Сколько миллиграмм вещества содержится в порции. Порцией может считаться любая единица" +
                " препарата, будь-то таблетка, капля, чайная или столовая ложки и т.д.")
        @NotNull
        @DecimalMin("0.1")
        @DecimalMax("1000")
        Float milligramsInPortion,
        @Schema(description = "Количество порцией по-умолчанию. Нужно для того, чтобы каждый раз не заполнять кол-во" +
                " порций при подсчете, если оно почти всегда одинаковое для препарата у пользователя")
        @Min(1)
        @Max(20)
        Integer defaultPortions
) implements PatientTemplateEntityDTO {
}
