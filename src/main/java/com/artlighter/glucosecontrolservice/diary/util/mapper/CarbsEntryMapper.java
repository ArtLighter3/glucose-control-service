package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.CarbsEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

@Component
public class CarbsEntryMapper extends AbstractEntryMapper<CarbsEntry, CarbsEntryDTO> {

    public CarbsEntryMapper(DecimalFormat diaryEntryFloatValueOutputFormat) {
        super(diaryEntryFloatValueOutputFormat);
    }

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
    }

    @Override
    protected CarbsEntry createEntry() {
        return new CarbsEntry();
    }
}
