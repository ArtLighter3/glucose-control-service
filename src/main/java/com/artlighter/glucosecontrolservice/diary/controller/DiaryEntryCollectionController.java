package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("api/patients/{userId}/entries")
public class DiaryEntryCollectionController {
    private DiaryEntryService diaryEntryService;
    private PatientProfileService patientProfileService;

    public DiaryEntryCollectionController(DiaryEntryService diaryEntryService,
                                          PatientProfileService patientProfileService) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_ALL') or " +
            "(hasAuthority('GLUCOSE_SHOW_OWN') and " +
            "@resourceAccessInspector.checkIfCurrentUserHasAccess(#userId, authentication)) or " +
            "(hasAuthority('GLUCOSE_SHOW_ATTACHED') and @resourceAccessInspector.checkIfDoctorHasAccess())")
    public List<DiaryEntry> getAllEntries(@PathVariable int userId,
                                                 @RequestParam(required = false) Instant from,
                                                 @RequestParam(required = false) Instant to) {

        return diaryEntryService.getDiaryEntriesOfType(null,
                patientProfileService.getByUserId(userId), from, to);
    }
}
