package com.artlighter.glucosecontrolservice.diary.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinProfileDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.util.mapper.InsulinProfileMapper;
import com.artlighter.glucosecontrolservice.diary.dto.PatientProfileDTO;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.diary.util.mapper.PatientProfileMapper;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients/{userId}/patient-profile")
public class PatientProfileController {
    private PatientProfileService patientProfileService;
    private PatientProfileMapper patientProfileMapper;

    public PatientProfileController(PatientProfileService patientProfileService,
                                    PatientProfileMapper patientProfileMapper) {
        this.patientProfileService = patientProfileService;
        this.patientProfileMapper = patientProfileMapper;
    }

    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, " +
            "'ROLE_PATIENT', #userId, authentication)")
    public PatientProfileDTO getPatientProfile(@PathVariable int userId) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null)
            throw new ResourceNotFoundException("Patient profile for user " + userId + " not found");

        return patientProfileMapper.mapToDTO(patientProfile);
    }

    @PutMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, " +
            "'ROLE_PATIENT', #userId, authentication)")
    public PatientProfileDTO putPatientProfile(@PathVariable int userId,
                                               @RequestBody @Valid PatientProfileDTO patientProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "Patient profile is invalid");

        patientProfileService.updateProfileForPatient(patientProfileMapper.mapToInternal(patientProfileDTO),
                userId);
        return patientProfileDTO;
    }
}
