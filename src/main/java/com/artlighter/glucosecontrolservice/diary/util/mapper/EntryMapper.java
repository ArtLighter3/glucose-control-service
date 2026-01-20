package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.general.DTOMapper;

public interface EntryMapper<INT extends DiaryEntry, EXT> extends DTOMapper<INT, EXT> {
    INT mapToInternal(DiaryEntryDeleteDTO entryDeletionDTO);
}
