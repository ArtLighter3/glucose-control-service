package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MealEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MealEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.EntryMapper;
import com.artlighter.glucosecontrolservice.diary.util.mapper.MealEntryMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class MealEntryController extends AbstractDiaryEntryController<MealEntry, MealEntryDTO> {
    public MealEntryController(DiaryEntryService diaryEntryService,
                               PatientProfileService patientProfileService,
                               MealEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @GetMapping("/meal")
    public List<MealEntryDTO> getDiaryEntries(int userId, Instant from, Instant to) {
        return super.getDiaryEntries(userId, from, to);
    }

    @Override
    @PostMapping("/meal")
    public MealEntryDTO postDiaryEntry(int userId, MealEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @PutMapping("/meal")
    public MealEntryDTO putDiaryEntry(int userId, MealEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @DeleteMapping("/meal")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.MEAL_ENTRY;
    }
}
