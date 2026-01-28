package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;

import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public abstract class AbstractEntryMapper<INT extends DiaryEntry, EXT> implements EntryMapper<INT, EXT> {

    @Override
    public INT mapToInternal(EXT entryDTO) {
        INT entry = createEntry();
        fillFields(entry, entryDTO);
        entry.setCommitedAt(entry.getCommitedAt().truncatedTo(ChronoUnit.MINUTES));
        return entry;
    }

    @Override
    public EXT mapToDTO(INT internal) {
        return mapToDTO(internal, ZoneOffset.UTC);
    }

    @Override
    public INT mapToInternal(DiaryEntryDeleteDTO entryDeletionDTO) {
        INT entry = createEntry();
        // При удалении мы не срезаем секунды у отметки. Иначе, если в БД окажется значение
        // с секундами, то его будет невозможно удалить без вмешательства в БД (в ID входит отметка, и отметка без
        // срезанных секунд не будет равна отметке с секундами, запись не будет найдена)
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
