package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.InsulinEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.InsulinEntryMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class InsulinEntryController extends AbstractDiaryEntryController<InsulinEntry, InsulinEntryDTO> {
    public InsulinEntryController(DiaryEntryService diaryEntryService,
                                  PatientProfileService patientProfileService,
                                  InsulinEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @GetMapping("/insulin")
    public List<InsulinEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @PostMapping("/insulin")
    public InsulinEntryDTO postDiaryEntry(int userId, InsulinEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @PutMapping("/insulin")
    public InsulinEntryDTO putDiaryEntry(int userId, InsulinEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @DeleteMapping("/insulin")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.INSULIN_ENTRY;
    }
}
