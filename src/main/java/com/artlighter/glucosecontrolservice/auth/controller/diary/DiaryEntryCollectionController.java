package com.artlighter.glucosecontrolservice.auth.controller.diary;

import com.artlighter.glucosecontrolservice.diary.dto.InCollectionDiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.dto.InCollectionDiaryEntryExceptionDTO;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.diary.service.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.dto.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.mapper.DiaryEntryCollectionMapper;
import jakarta.validation.Valid;
import org.hibernate.mapping.Collection;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
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

    @PostMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('GLUCOSE_ADD_ALL', 'GLUCOSE_ADD_ATTACHED', " +
            "'GLUCOSE_ADD_OWN', #userId, authentication)")
    public List<DiaryEntryDTO> postAllEntries(@PathVariable int userId,
                   @RequestBody @Valid List<InCollectionDiaryEntryDTO> entries, BindingResult bindingResult) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        //TODO
        if (bindingResult.hasErrors()) {}

        List<DiaryEntry> added
                = diaryEntryService.addDiaryEntries(collectionMapper.mapToInternal(entries, patientProfile),
                        patientProfile, true);

        return collectionMapper.mapToDTO(added, patientProfile, ZoneOffset.UTC);
    }
}
