package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.CarbsEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.CarbsEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.CarbsEntryMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class CarbsEntryController extends AbstractDiaryEntryController<CarbsEntry, CarbsEntryDTO> {
    public CarbsEntryController(DiaryEntryService diaryEntryService,
                                PatientProfileService patientProfileService,
                                CarbsEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @GetMapping("/carbs")
    public List<CarbsEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @PostMapping("/carbs")
    public CarbsEntryDTO postDiaryEntry(int userId, CarbsEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @PutMapping("/carbs")
    public CarbsEntryDTO putDiaryEntry(int userId, CarbsEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @DeleteMapping("/carbs")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.CARBS_ENTRY;
    }
}
