package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.entity.enumeration.MeasurementType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.OffsetDateTime;

public record GlucoseEntryDTO(
        @DecimalMin("0.5")
        @DecimalMax("40.0")
        @NotNull
        Float value,
        @NotNull
        OffsetDateTime commitedAt,
        MeasurementType type,
        @Length(max = 500)
        String notes,
        String glucoseUnits)

        implements DiaryEntryDTO {
}
