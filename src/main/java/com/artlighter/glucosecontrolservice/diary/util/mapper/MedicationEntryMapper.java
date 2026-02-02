package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class MedicationEntryMapper extends AbstractEntryMapper<MedicationEntry, MedicationEntryDTO> {

    @Override
    public MedicationEntryDTO mapToDtoWithUnitConversion(MedicationEntry entry, PatientProfile patientProfile,
                                                         ZoneOffset outputZoneOffset) {
        return new MedicationEntryDTO(entry.getValue(), entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getMedicationName(), entry.getNotes());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(MedicationEntry entry, MedicationEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());

        entry.setMedicationName(entryDTO.name());
    }

    @Override
    protected MedicationEntry createEntry() {
        return new MedicationEntry();
    }
}
