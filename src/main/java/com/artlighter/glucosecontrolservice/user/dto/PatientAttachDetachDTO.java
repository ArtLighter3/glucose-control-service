package com.artlighter.glucosecontrolservice.user.dto;

import jakarta.validation.constraints.NotNull;

public record PatientAttachDetachDTO(
        @NotNull
        Integer patientId
) {
}
