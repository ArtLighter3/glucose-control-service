package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

public record MedicationEntryDTO(
        @DecimalMin("0")
        @DecimalMax("1000")
        @NotNull
        Float value,
        @NotNull
        OffsetDateTime commitedAt,
        @NotNull
        @Length(max = 100)
        String name,
        @Length(max = 500)
        String notes)

        implements DiaryEntryDTO {
}
