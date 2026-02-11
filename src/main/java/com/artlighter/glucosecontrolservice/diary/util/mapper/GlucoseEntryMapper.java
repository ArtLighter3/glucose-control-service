package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.ZoneOffset;

@Component
public class GlucoseEntryMapper extends AbstractEntryMapper<GlucoseEntry, GlucoseEntryDTO> {

    public GlucoseEntryMapper(DecimalFormat diaryEntryFloatValueOutputFormat) {
        super(diaryEntryFloatValueOutputFormat);
    }

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

        entry.setMeasurementType(entryDTO.type());
    }

    @Override
    protected GlucoseEntry createEntry() {
        return new GlucoseEntry();
    }

}
