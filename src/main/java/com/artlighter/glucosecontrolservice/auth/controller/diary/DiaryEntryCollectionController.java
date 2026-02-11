package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.mapper.DiaryEntryCollectionMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("api/patients/{userId}/entries")
public class DiaryEntryCollectionController {
    private DiaryEntryService diaryEntryService;
    private PatientProfileService patientProfileService;
    private DiaryEntryCollectionMapper collectionMapper;

    public DiaryEntryCollectionController(DiaryEntryService diaryEntryService,
                                          PatientProfileService patientProfileService,
                                          DiaryEntryCollectionMapper collectionMapper) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
        this.collectionMapper = collectionMapper;
    }

    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_SHOW_ALL', 'GLUCOSE_SHOW_ATTACHED', " +
            "'GLUCOSE_SHOW_OWN', #userId, authentication)")
    public List<DiaryEntryDTO> getAllEntries(@PathVariable int userId,
                                             @RequestParam(required = false) Instant from,
                                             @RequestParam(required = false) Instant to,
                                             @RequestParam(required = false) ZoneOffset outputZoneOffset) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        List<DiaryEntry> entries = diaryEntryService.getDiaryEntriesOfType(null, patientProfile, from, to);

        return collectionMapper.mapToDTO(entries, patientProfile, outputZoneOffset);
    }
}
