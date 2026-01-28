package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class GlucoseEntryMapper extends AbstractEntryMapper<GlucoseEntry, GlucoseEntryDTO> {

    @Override
    public GlucoseEntryDTO mapToDTO(GlucoseEntry entry, ZoneOffset outputZoneOffset) {
        return new GlucoseEntryDTO(entry.getValue(), entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getMeasurementType(), entry.getNotes());
    }

    @Override
    protected void fillFields(GlucoseEntry entry, GlucoseEntryDTO entryDTO) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());

        entry.setMeasurementType(entryDTO.type());
    }

    @Override
    protected GlucoseEntry createEntry() {
        return new GlucoseEntry();
    }

}
