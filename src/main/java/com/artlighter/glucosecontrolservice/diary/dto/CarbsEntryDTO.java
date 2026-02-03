package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

public record CarbsEntryDTO(
        @DecimalMin("0")
        @DecimalMax("300")
        @NotNull
        Float value,
        @NotNull
        OffsetDateTime commitedAt,
        @Length(max = 500)
        String notes,
        String carbsUnits)

        implements DiaryEntryDTO {
}
