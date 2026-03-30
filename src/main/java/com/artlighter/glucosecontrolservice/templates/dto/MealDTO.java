package com.artlighter.glucosecontrolservice.templates.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(name = "Meal", description = "Сохраненное пользователем блюдо с указанным кол-вом углеводов на 100 г. веса")
public record MealDTO(
        @Schema(description = "Имя блюда")
        @NotBlank
        @Length(max = 200)
        String name,
        @Schema(description = "Количество углеводов (в граммах) на 100 г. веса", example = "50")
        @NotNull
        @DecimalMin("0")
        @DecimalMax("300")
        Float carbsPer100Grams
) implements PatientTemplateEntityDTO {
}
