package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

@Component
public class InsulinEntryMapper extends AbstractEntryMapper<InsulinEntry, InsulinEntryDTO> {

    public InsulinEntryMapper(DecimalFormat diaryEntryFloatValueOutputFormat) {
        super(diaryEntryFloatValueOutputFormat);
    }

    @Override
    public InsulinEntryDTO mapToDtoWithUnitConversion(InsulinEntry entry, PatientProfile patientProfile,
                                                      ZoneOffset outputZoneOffset) {
        return new InsulinEntryDTO(round(entry.getValue()), entry.getCommitedAt().atOffset(outputZoneOffset),
                entry.getInsulinType(), entry.getNotes());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(InsulinEntry entry, InsulinEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setInsulinType(entryDTO.insulinType());
    }

    @Override
    protected InsulinEntry createEntry() {
        return new InsulinEntry();
    }
}
