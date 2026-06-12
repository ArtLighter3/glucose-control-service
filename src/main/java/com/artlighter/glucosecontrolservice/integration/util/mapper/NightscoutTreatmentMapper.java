package com.artlighter.glucosecontrolservice.integration.util.mapper;

import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.enumeration.InsulinType;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.integration.dto.NightscoutTreatmentDTO;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class NightscoutTreatmentMapper implements DTOMapper<List<DiaryEntry>, NightscoutTreatmentDTO> {
    @Override
    public NightscoutTreatmentDTO mapToDTO(List<DiaryEntry> internal) {
        return null;
    }

    @Override
    public List<DiaryEntry> mapToInternal(NightscoutTreatmentDTO externalDTO) {
        List<DiaryEntry> entries = new ArrayList<>();

        if (externalDTO.carbs() != null && externalDTO.carbs() > 0f)
            entries.add(createCarbsEntry(externalDTO.createdAt(), externalDTO.carbs(), externalDTO.notes()));

        if (externalDTO.glucose() != null && externalDTO.glucose() > 0f)
            entries.add(createGlucoseEntry(externalDTO.createdAt(), externalDTO.glucose(), externalDTO.notes(),
                    externalDTO.units()));

        if (externalDTO.insulin() != null && externalDTO.insulin() > 0f)
            entries.add(createInsulinEntry(externalDTO.createdAt(), externalDTO.insulin(), externalDTO.notes(),
                    externalDTO.eventType()));

        return entries;
    }
//
    private CarbsEntry createCarbsEntry(String commitedAt, Float carbs, String notes) {
        CarbsEntry carbsEntry = new CarbsEntry();

        fill(carbsEntry, commitedAt, carbs, notes);

        return carbsEntry;
    }
//
    private GlucoseEntry createGlucoseEntry(String commitedAt, Float glucose, String notes, String units) {
        GlucoseEntry glucoseEntry = new GlucoseEntry();

        fill(glucoseEntry,
                commitedAt,
                units.equals("mmol") ? glucose :
                        (float) GlucoseUnit.MILLIGRAMS_PER_DECILITER.convertToMmolPerLiter(glucose),
                notes);

        return glucoseEntry;
    }

    private InsulinEntry createInsulinEntry(String commitedAt, Float insulin, String notes, String eventType) {
        InsulinEntry insulinEntry = new InsulinEntry();

        fill(insulinEntry, commitedAt, insulin, eventType);

        if (eventType == null) insulinEntry.setInsulinType(InsulinType.SHORT);
        else if (eventType.equals("Temp Basal") || eventType.equals("Permanent Basal"))
            insulinEntry.setInsulinType(InsulinType.LONG);
        else insulinEntry.setInsulinType(InsulinType.SHORT);

        return insulinEntry;
    }

    private void fill(DiaryEntry entry, String commitedAt, Float value, String notes) {
        entry.setCommitedAt(Instant.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(commitedAt))
                .truncatedTo(ChronoUnit.MINUTES));
        if (notes != null && !notes.equals("<none>"))
            entry.setNotes(notes);
        entry.setValue(value);

    }
}
