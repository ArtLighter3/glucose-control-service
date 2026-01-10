package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.auth.util.convert.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.ExceptionDTO;
import com.artlighter.glucosecontrolservice.auth.util.exception.NotCurrentUsersInfoException;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.user.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/diary")
public class DiaryController {
    private DiaryEntryService diaryEntryService;
    private PatientProfileService patientProfileService;

    public DiaryController(DiaryEntryService diaryEntryService, PatientProfileService patientProfileService) {
        this.diaryEntryService = diaryEntryService;
        this.patientProfileService = patientProfileService;
    }

//    @PostMapping("/{userId}/")
//    public HttpStatus postMeasurement(@RequestBody DiaryEntryDTO measurement) {
//        DiaryEntry entry = diaryService.saveMeasurement(convertFromDTO(measurement));
//        if (entry != null) {
//            return HttpStatus.CREATED;
//        }
//        return HttpStatus.INTERNAL_SERVER_ERROR;
//    }

    @GetMapping("/{userId}/entries")
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntry> getPatientEntries(@PathVariable int userId,
                                              @AuthenticationPrincipal ServiceUserDetails userDetails) {
        if (userId != userDetails.getId())
            throw new NotCurrentUsersInfoException("You don't have access to this user's info");

        return diaryEntryService.getAllPatientEntries(patientProfileService.getByUserId(userId));
    }

    @ExceptionHandler(NotCurrentUsersInfoException.class)
    public ResponseEntity<ExceptionDTO> notCurrentUsersInfoException(NotCurrentUsersInfoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(DTOConvertUtils.createOutputException(HttpStatus.FORBIDDEN, ex));
    }

//    private DiaryEntry convertFromDTO(DiaryEntryDTO dto) {
//        return new DiaryEntry(dto.measurement(), dto.date(), dto.notes(), new UserDTO(dto.username(), "", Collections.emptySet()));
//    }
//
//    private DiaryEntryDTO convertToDTO(DiaryEntry diaryEntry) {
//        return new DiaryEntryDTO(diaryEntry.getMeasurement(), diaryEntry.getDate(), diaryEntry.getNotes(), diaryEntry.getUser().username());
//    }
}
