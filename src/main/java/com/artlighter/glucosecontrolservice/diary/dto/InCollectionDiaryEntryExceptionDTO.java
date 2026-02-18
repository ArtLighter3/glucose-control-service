package com.artlighter.glucosecontrolservice.diary.dto;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;

import java.util.Map;

public record InCollectionDiaryEntryExceptionDTO(
    DiaryEntryType type,
    DiaryEntryDTO entryInfo,
    Map<String, String> validationErrors
) {
}
