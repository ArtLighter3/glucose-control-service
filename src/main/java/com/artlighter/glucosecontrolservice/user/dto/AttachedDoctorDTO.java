package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AttachedDoctor", description = "Информация о враче для его больного")
public record AttachedDoctorDTO(
        @Schema(description = "Фамилия")
        String lastName,
        @Schema(description = "Имя")
        String firstName,
        @Schema(description = "Отчество, если есть")
        String middleName,
        @Schema(description = "Личный код врача (для открепления)")
        String doctorCode
) {
}
