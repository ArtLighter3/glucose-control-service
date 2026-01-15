package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;

public interface EntryMapper<INT extends DiaryEntry, EXT> {
    EXT mapToDTO(INT entry);
    INT mapToInternal(EXT entryDTO);
    INT mapToInternal(DiaryEntryDeleteDTO entryDeletionDTO);
}
