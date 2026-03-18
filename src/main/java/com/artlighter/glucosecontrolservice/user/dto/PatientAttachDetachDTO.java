package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "PatientAttachDetach", description = "Обертка для отправления информации о больном, которого " +
        "необходимо прикрепить к врачу")
public record PatientAttachDetachDTO(
        @Schema(description = "ID больного, прикрепляемого к врачу")
        @NotNull
        Integer patientId
) {
}
