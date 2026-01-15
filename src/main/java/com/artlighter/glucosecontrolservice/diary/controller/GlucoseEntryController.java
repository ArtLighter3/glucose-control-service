package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.GlucoseEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.GlucoseEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.GlucoseEntryMapper;
import com.artlighter.glucosecontrolservice.diary.util.mapper.InsulinEntryMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
public class GlucoseEntryController extends AbstractDiaryEntryController<GlucoseEntry, GlucoseEntryDTO> {
    public GlucoseEntryController(DiaryEntryService diaryEntryService,
                                  PatientProfileService patientProfileService,
                                  GlucoseEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @GetMapping("/glucose")
    public List<GlucoseEntryDTO> getDiaryEntries(int userId, Instant from, Instant to) {
        return super.getDiaryEntries(userId, from, to);
    }

    @Override
    @PostMapping("/glucose")
    public GlucoseEntryDTO postDiaryEntry(int userId, GlucoseEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @PutMapping("/glucose")
    public GlucoseEntryDTO putDiaryEntry(int userId, GlucoseEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @DeleteMapping("/glucose")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.GLUCOSE_ENTRY;
    }
}
