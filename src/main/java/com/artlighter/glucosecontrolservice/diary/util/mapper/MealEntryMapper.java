package com.artlighter.glucosecontrolservice.diary.util.mapper;

import com.artlighter.glucosecontrolservice.diary.dto.MealEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MealEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import org.springframework.stereotype.Component;

@Component
public class MealEntryMapper extends AbstractEntryMapper<MealEntry, MealEntryDTO> {

    @Override
    public MealEntryDTO mapToDTO(MealEntry entry) {
        return new MealEntryDTO(entry.getValue(), entry.getCommitedAt(), entry.getNotes());
    }

    @Override
    protected void fillFields(MealEntry entry, MealEntryDTO entryDTO) {
        entry.setValue(entryDTO.value());
        entry.setCommitedAt(entryDTO.commitedAt());
        entry.setNotes(entryDTO.notes());
    }

    @Override
    protected MealEntry createEntry() {
        return new MealEntry();
    }
}
