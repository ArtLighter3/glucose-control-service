package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

public record InsulinEntryDTO(
        @DecimalMin("1")
        @DecimalMax("100")
        @NotNull
        Float value,
        @NotNull
        OffsetDateTime commitedAt,
        @NotNull
        InsulinType type,
        @Length(max = 500)
        String notes)

        implements DiaryEntryDTO {
}
