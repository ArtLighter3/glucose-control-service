package com.artlighter.glucosecontrolservice.diary.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record DiaryEntryDeleteDTO (
    @NotNull
    Instant commitedAt
) {

}
