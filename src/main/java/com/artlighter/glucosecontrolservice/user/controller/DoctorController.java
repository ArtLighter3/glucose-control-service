package com.artlighter.glucosecontrolservice.user.controller;

import com.artlighter.glucosecontrolservice.user.dto.PatientAttachDetachDTO;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors/{userId}")
public class DoctorController {
    private DoctorProfileService doctorProfileService;

    public DoctorController(DoctorProfileService doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT_ATTACH')")
    public PatientAttachDetachDTO attachPatient(@PathVariable int userId,
                                                @RequestBody PatientAttachDetachDTO attachDetachDTO) {
        doctorProfileService.attachPatientToDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('PATIENT_ATTACH')")
    public PatientAttachDetachDTO detachPatient(@PathVariable int userId,
                                                @RequestBody PatientAttachDetachDTO attachDetachDTO) {
        doctorProfileService.detachPatientFromDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }
}
