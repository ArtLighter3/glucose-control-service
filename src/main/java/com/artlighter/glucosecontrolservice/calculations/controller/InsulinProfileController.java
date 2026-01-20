package com.artlighter.glucosecontrolservice.calculations.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.calculations.dto.InsulinProfileDTO;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.service.InsulinProfileService;
import com.artlighter.glucosecontrolservice.calculations.util.mapper.InsulinProfileMapper;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients/{userId}/insulin-profile")
public class InsulinProfileController {
    private PatientProfileService patientProfileService;
    private InsulinProfileService insulinProfileService;
    private InsulinProfileMapper insulinProfileMapper;

    public InsulinProfileController(PatientProfileService patientProfileService,
                                    InsulinProfileService insulinProfileService,
                                    InsulinProfileMapper insulinProfileMapper) {
        this.patientProfileService = patientProfileService;
        this.insulinProfileService = insulinProfileService;
        this.insulinProfileMapper = insulinProfileMapper;
    }

    @GetMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_SHOW_ALL', " +
            "'INSULIN_PROFILE_SHOW_ATTACHED', 'INSULIN_PROFILE_SHOW_OWN', #userId, authentication)")
    public InsulinProfileDTO getInsulinProfile(@PathVariable int userId) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null)
            throw new ResourceNotFoundException("Patient profile for user " + userId + " not found");

        InsulinProfile insulinProfile = insulinProfileService.getByPatientProfileId(patientProfile.getId());
        if (insulinProfile == null)
            throw new ResourceNotFoundException("Insulin profile for user " + userId + " not found");

        return insulinProfileMapper.mapToDTO(insulinProfile);
    }

    @PostMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_ADD_ALL', " +
            "'INSULIN_PROFILE_ADD_ATTACHED', 'INSULIN_PROFILE_ADD_OWN', #userId, authentication)")
    public InsulinProfileDTO postInsulinProfile(@PathVariable int userId,
                                               @RequestBody @Valid InsulinProfileDTO insulinProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "Insulin profile is invalid");

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null)
            throw new ResourceNotFoundException("Patient profile for user " + userId + " not found");

        insulinProfileService.createInsulinProfile(insulinProfileMapper.mapToInternal(insulinProfileDTO),
                patientProfile.getId());
        return insulinProfileDTO;
    }

    @PutMapping
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('INSULIN_PROFILE_UPDATE_ALL', " +
            "'INSULIN_PROFILE_UPDATE_ATTACHED', 'INSULIN_PROFILE_UPDATE_OWN', #userId, authentication)")
    public InsulinProfileDTO putInsulinProfile(@PathVariable int userId,
                                               @RequestBody @Valid InsulinProfileDTO insulinProfileDTO,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "Insulin profile is invalid");

        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null)
            throw new ResourceNotFoundException("Patient profile for user " + userId + " not found");

        insulinProfileService.updateInsulinProfile(insulinProfileMapper.mapToInternal(insulinProfileDTO),
                patientProfile.getId());
        return insulinProfileDTO;
    }
}
