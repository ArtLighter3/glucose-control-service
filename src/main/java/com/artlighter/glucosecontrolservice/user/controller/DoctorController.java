package com.artlighter.glucosecontrolservice.user.controller;

import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.dto.PatientAttachDetachDTO;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors/{userId}")
public class DoctorController {
    private DoctorProfileService doctorProfileService;

    public DoctorController(DoctorProfileService doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT_ATTACH_DETACH')")
    public PatientAttachDetachDTO attachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "");

        doctorProfileService.attachPatientToDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('PATIENT_ATTACH_DETACH')")
    public PatientAttachDetachDTO detachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationIsFailedException(bindingResult, "");

        doctorProfileService.detachPatientFromDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }
}
