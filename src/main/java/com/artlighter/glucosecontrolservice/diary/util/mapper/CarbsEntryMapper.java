package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.CarbsEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class CarbsEntryMapper extends AbstractEntryMapper<CarbsEntry, CarbsEntryDTO> {

    @Override
    public CarbsEntryDTO mapToDTO(CarbsEntry entry, ZoneOffset outputZoneOffset) {
        return new CarbsEntryDTO(entry.getValue(), entry.getCommitedAt().atOffset(outputZoneOffset), entry.getNotes());
    }

    @Override
    protected void fillFields(CarbsEntry entry, CarbsEntryDTO entryDTO) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());
    }

    @Override
    protected CarbsEntry createEntry() {
        return new CarbsEntry();
    }
}
