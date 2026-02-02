package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.GlucoseUnit;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class GlucoseEntryMapper extends AbstractEntryMapper<GlucoseEntry, GlucoseEntryDTO> {

    @Override
    public GlucoseEntryDTO mapToDtoWithUnitConversion(GlucoseEntry internal, PatientProfile patientProfile,
                                                      ZoneOffset outputZoneOffset) {
        return new GlucoseEntryDTO(round(patientProfile.getGlucoseUnit().convertFromMmolPerLiter(internal.getValue())),
                internal.getCommitedAt().atOffset(outputZoneOffset),
                internal.getMeasurementType(),
                internal.getNotes(),
                patientProfile.getGlucoseUnit().name());
    }

    @Override
    protected void fillFieldsOfInternalWithUnitConversion(GlucoseEntry entry, GlucoseEntryDTO entryDTO,
                                                          PatientProfile patientProfile) {
        entry.setValue(patientProfile.getGlucoseUnit().convertToMmolPerLiter(entryDTO.value()));
        entry.setCommitedAt(entryDTO.commitedAt().toInstant());
        entry.setNotes(entryDTO.notes());

        entry.setMeasurementType(entryDTO.type());
    }

    @Override
    protected GlucoseEntry createEntry() {
        return new GlucoseEntry();
    }

}
