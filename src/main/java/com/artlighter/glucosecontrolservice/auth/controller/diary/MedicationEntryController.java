package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDeleteDTO;
import com.artlighter.glucosecontrolservice.diary.dto.MedicationEntryDTO;
import com.artlighter.glucosecontrolservice.diary.entity.entry.MedicationEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import com.artlighter.glucosecontrolservice.diary.util.mapper.MedicationEntryMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
public class MedicationEntryController extends AbstractDiaryEntryController<MedicationEntry, MedicationEntryDTO> {
    public MedicationEntryController(DiaryEntryService diaryEntryService,
                                     PatientProfileService patientProfileService,
                                     MedicationEntryMapper entryMapper) {
        super(diaryEntryService, patientProfileService, entryMapper);
    }

    @Override
    @GetMapping("/medication")
    public List<MedicationEntryDTO> getDiaryEntries(int userId, Instant from, Instant to, ZoneOffset outputZoneOffset) {
        return super.getDiaryEntries(userId, from, to, outputZoneOffset);
    }

    @Override
    @PostMapping("/medication")
    public MedicationEntryDTO postDiaryEntry(int userId, MedicationEntryDTO entryDTO, BindingResult bindingResult) {
        return super.postDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @PutMapping("/medication")
    public MedicationEntryDTO putDiaryEntry(int userId, MedicationEntryDTO entryDTO, BindingResult bindingResult) {
        return super.putDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    @DeleteMapping("/medication")
    public DiaryEntryDeleteDTO deleteDiaryEntry(int userId, DiaryEntryDeleteDTO entryDTO, BindingResult bindingResult) {
        return super.deleteDiaryEntry(userId, entryDTO, bindingResult);
    }

    @Override
    protected DiaryEntryType getEntryType() {
        return DiaryEntryType.MEDICATION_ENTRY;
    }
}
