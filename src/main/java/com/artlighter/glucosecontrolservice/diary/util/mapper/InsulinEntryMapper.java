package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class InsulinEntryMapper extends AbstractEntryMapper<InsulinEntry, InsulinEntryDTO> {

    @Override
    public InsulinEntryDTO mapToDtoWithUnitConversion(InsulinEntry entry, PatientProfile patientProfile,
                                                      ZoneOffset outputZoneOffset) {
        return new InsulinEntryDTO(entry.getValue(), entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getInsulinType(), entry.getNotes());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(InsulinEntry entry, InsulinEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());

        entry.setInsulinType(entryDTO.type());
    }

    @Override
    protected InsulinEntry createEntry() {
        return new InsulinEntry();
    }
}
