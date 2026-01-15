package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;

public abstract class AbstractEntryMapper<INT extends DiaryEntry, EXT> implements EntryMapper<INT, EXT> {

    @Override
    public INT mapToInternal(EXT entryDTO) {
        INT entry = createEntry();
        fillFields(entry, entryDTO);
        return entry;
    }

    @Override
    public INT mapToInternal(DiaryEntryDeleteDTO entryDeletionDTO) {
        INT entry = createEntry();
        entry.setCommitedAt(entryDeletionDTO.commitedAt());
        return entry;
    }

    /**
     * Функция должна заполнять поля записи дневника INT, используя поля EXT
     * @param entry
     * @param entryDTO
     */
    protected abstract void fillFields(INT entry, EXT entryDTO);

    /**
     * Функция должна создавать экземпляр записи дневника INT
     * @return экземпляр записи дневника определенного типа
     */
    protected abstract INT createEntry();
}
