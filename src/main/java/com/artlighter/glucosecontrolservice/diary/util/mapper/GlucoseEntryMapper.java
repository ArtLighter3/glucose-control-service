package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.stereotype.Component;

@Component
public class GlucoseEntryMapper extends AbstractEntryMapper<GlucoseEntry, GlucoseEntryDTO> {

    @Override
    public GlucoseEntryDTO mapToDTO(GlucoseEntry entry) {
        return new GlucoseEntryDTO(entry.getValue(), entry.getCommitedAt(),
                entry.getMeasurementType(), entry.getNotes());
    }

    @Override
    protected void fillFields(GlucoseEntry entry, GlucoseEntryDTO entryDTO) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        entry.setMeasurementType(entryDTO.type());
    }

    @Override
    protected GlucoseEntry createEntry() {
        return new GlucoseEntry();
    }

}
