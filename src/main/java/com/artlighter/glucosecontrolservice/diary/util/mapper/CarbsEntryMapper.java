package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.CarbsEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.CarbsUnit;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Locale;

@Component
public class CarbsEntryMapper extends AbstractEntryMapper<CarbsEntry, CarbsEntryDTO> {

    @Override
    public CarbsEntryDTO mapToDtoWithUnitConversion(CarbsEntry entry, PatientProfile patientProfile,
                                                    ZoneOffset outputZoneOffset) {
        return new CarbsEntryDTO(round(patientProfile.getCarbsUnit().convertFromGrams(entry.getValue())),
                entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getNotes(),
                patientProfile.getCarbsUnit().name());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(CarbsEntry entry, CarbsEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setValue(patientProfile.getCarbsUnit().convertToGrams(entryDTO.value()));
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());
    }

    @Override
    protected CarbsEntry createEntry() {
        return new CarbsEntry();
    }
}
