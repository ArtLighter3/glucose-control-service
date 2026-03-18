package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AttachedPatient", description = "Информация о прикрепленном к врачу больном")
public record AttachedPatientDTO(
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Отчество, если есть")
        String middleName,
        @Schema(description = "ID больного в системе")
        Integer patientId,
        @Schema(description = "ID профиля больного в системе")
        Integer patientProfileId,
        @Schema(description = "Эл. почта")
        String email
) {
}
