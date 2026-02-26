package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(name = "InCollectionDiaryEntry", description = "Запись дневника любого типа с информацией о ее типе")
public record InCollectionDiaryEntryDTO(
        @Schema(description = "Тип записи дневника (принятие углеводов, измерение глюкозы и т.д.)",
                example = "GLUCOSE_ENTRY")
        @NotNull
        DiaryEntryType type,
        @NotNull
        @Valid
        DiaryEntryDTO entryInfo) {
}
