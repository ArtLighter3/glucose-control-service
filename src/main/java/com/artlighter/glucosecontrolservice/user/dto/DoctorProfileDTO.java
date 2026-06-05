package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DoctorProfile", description = "Профиль врача с его уникальным кодом")
public record DoctorProfileDTO(
        @Schema(description = "Личный уникальный код врача, используемый пациентами для самостоятельного прикрепления")
        String personalCode
) {
}
