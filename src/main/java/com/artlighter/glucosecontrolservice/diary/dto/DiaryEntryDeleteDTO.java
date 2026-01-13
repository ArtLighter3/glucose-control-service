package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record DiaryEntryDeleteDTO (
    @NotNull(message = "Timestamp of measurement must be provided")
    Instant commitedAt
) {

}
