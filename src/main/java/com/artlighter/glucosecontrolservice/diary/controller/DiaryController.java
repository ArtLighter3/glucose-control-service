package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryDTO;
import com.artlighter.glucosecontrolservice.diary.DiaryService;
import com.artlighter.glucosecontrolservice.diary.entity.DiaryEntry;
import com.artlighter.glucosecontrolservice.user.UserDTO;
import com.artlighter.glucosecontrolservice.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {
    private DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public HttpStatus postMeasurement(@RequestBody DiaryEntryDTO measurement) {
        DiaryEntry entry = diaryService.saveMeasurement(convertFromDTO(measurement));
        if (entry != null) {
            return HttpStatus.CREATED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GLUCOSE_SHOW_OWN')")
    public List<DiaryEntryDTO> getUserMeasurements(@RequestParam(required = false) String username) {
        if (username == null) {
            return diaryService.getAllMeasurements().stream().map(entry -> convertToDTO(entry)).toList();
        }

        return diaryService.getAllUserMeasurements(new UserDTO(username, "", Collections.emptySet()))
                .stream().map(entry -> convertToDTO(entry)).toList();
    }

    private DiaryEntry convertFromDTO(DiaryEntryDTO dto) {
        return new DiaryEntry(dto.measurement(), dto.date(), dto.notes(), new UserDTO(dto.username(), "", Collections.emptySet()));
    }

    private DiaryEntryDTO convertToDTO(DiaryEntry diaryEntry) {
        return new DiaryEntryDTO(diaryEntry.getMeasurement(), diaryEntry.getDate(), diaryEntry.getNotes(), diaryEntry.getUser().username());
    }
}
