package com.artlighter.glucosecontrolservice.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(name = "DoctorCodeWrapper", description = "Оболочка с личным кодом врача " +
        "для самостоятельного прикрепления больным")
public record DoctorCodeWrapperDTO(
        @NotBlank
        @Length(min = 8, max = 8)
        String doctorCode
) {
}
