package com.artlighter.glucosecontrolservice.calculations.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinCalculationRequestDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinResult;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinCalculationService;
import com.artlighter.glucosecontrolservice.diary.DiaryEntryService;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.entity.entry.InsulinEntry;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{userId}/insulin")
public class InsulinCalculationController {
    private PatientProfileService patientProfileService;
    private InsulinProfileService insulinProfileService;
    private InsulinCalculationService insulinCalculationService;
    private DiaryEntryService diaryEntryService;

    public InsulinCalculationController(PatientProfileService patientProfileService,
                                        InsulinProfileService insulinProfileService,
                                        InsulinCalculationService insulinCalculationService,
                                        DiaryEntryService diaryEntryService) {
        this.patientProfileService = patientProfileService;
        this.insulinProfileService = insulinProfileService;
        this.insulinCalculationService = insulinCalculationService;
        this.diaryEntryService = diaryEntryService;
    }

    @GetMapping("/calculate")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'INSULIN_CALCULATE'," +
            "#userId, authentication)")
    public InsulinResult calculate(@Valid InsulinCalculationRequestDTO calculationRequest, BindingResult bindingResult,
                                   @PathVariable int userId) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(patientProfile.getId());
        if (insulinProfile == null) throw new ResourceNotFoundException("insulin profile not found");

        Instant now = Instant.now();
        List<DiaryEntry> insulinEntries = diaryEntryService.getDiaryEntriesOfType(DiaryEntryType.INSULIN_ENTRY,
                patientProfile, now.minus(Duration.ofHours(12)), now);

        return insulinCalculationService.calculateInsulinDose(patientProfile, insulinProfile, null,
                calculationRequest.localTimeOfDay(), calculationRequest.carbs(),
                calculationRequest.glucose() != null ? calculationRequest.glucose() : 0f,
                calculationRequest.correction() != null ? calculationRequest.correction() : 0f);
    }
}
