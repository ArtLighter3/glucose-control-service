package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import org.springframework.stereotype.Component;

@Component
public class MedicationEntryMapper extends AbstractEntryMapper<MedicationEntry, MedicationEntryDTO> {

    @Override
    public MedicationEntryDTO mapToDTO(MedicationEntry entry) {
        return new MedicationEntryDTO(entry.getValue(), entry.getCommitedAt(),
                entry.getMedicationName(), entry.getNotes());
    }

    @Override
    protected void fillFields(MedicationEntry entry, MedicationEntryDTO entryDTO) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());

        entry.setMedicationName(entryDTO.name());
    }

    @Override
    protected MedicationEntry createEntry() {
        return new MedicationEntry();
    }
}
