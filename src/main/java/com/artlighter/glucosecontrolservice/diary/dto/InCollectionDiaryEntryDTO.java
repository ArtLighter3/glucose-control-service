package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record InCollectionDiaryEntryDTO(
        @NotNull
        DiaryEntryType type,
        @NotNull
        @Valid
        DiaryEntryDTO entryInfo) {
}
