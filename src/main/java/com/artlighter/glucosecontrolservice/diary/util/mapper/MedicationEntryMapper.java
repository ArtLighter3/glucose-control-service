package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

@Component
public class MedicationEntryMapper extends AbstractEntryMapper<MedicationEntry, MedicationEntryDTO> {

    public MedicationEntryMapper(DecimalFormat diaryEntryFloatValueOutputFormat) {
        super(diaryEntryFloatValueOutputFormat);
    }

    @Override
    public MedicationEntryDTO mapToDtoWithUnitConversion(MedicationEntry entry, PatientProfile patientProfile,
                                                         ZoneOffset outputZoneOffset) {
        return new MedicationEntryDTO(round(entry.getValue()), entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getMedicationName(), entry.getNotes());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(MedicationEntry entry, MedicationEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setMedicationName(entryDTO.name());
    }

    @Override
    protected MedicationEntry createEntry() {
        return new MedicationEntry();
    }
}
