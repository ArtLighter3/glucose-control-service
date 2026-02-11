package com.artlighter.glucosecontrolservice.auth.controller.user;

import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.dto.AttachedPatientDTO;
import com.artlighter.glucosecontrolservice.user.dto.PatientAttachDetachDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import com.artlighter.glucosecontrolservice.user.util.mapper.AttachedPatientMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors/{userId}")
public class DoctorController {
    private DoctorProfileService doctorProfileService;
    private AttachedPatientMapper attachedPatientMapper;

    public DoctorController(DoctorProfileService doctorProfileService, AttachedPatientMapper attachedPatientMapper) {
        this.doctorProfileService = doctorProfileService;
        this.attachedPatientMapper = attachedPatientMapper;
    }

    @GetMapping("/attached-patients")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('ATTACHED_PATIENT_SHOW_ALL', " +
            "null, 'ATTACHED_PATIENT_SHOW_OWN', #userId, authentication)")
    public Page<AttachedPatientDTO> getAttachedPatients(@PathVariable int userId,
                                                        @PageableDefault(sort = "user.username") Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.getAttachedPatients(userId, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @GetMapping("/attached-patients/search")
    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource('ATTACHED_PATIENT_SHOW_ALL', " +
            "null, 'ATTACHED_PATIENT_SHOW_OWN', #userId, authentication)")
    public Page<AttachedPatientDTO> getAttachedPatientsBySearchQuery(@PathVariable int userId,
                                                        @RequestParam("query") String query,
                                                        @PageableDefault(sort = "user.username") Pageable pageable) {
        Page<PatientProfile> attachedPatients = doctorProfileService.searchAttachedPatients(userId, query, pageable);

        return attachedPatients.map(attachedPatientMapper::mapToDTO);
    }

    @PostMapping("/attached-patients")
    @PreAuthorize("hasAuthority('ATTACHED_PATIENT_ATTACH_DETACH')")
    public PatientAttachDetachDTO attachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        //TODO а что если прикрепляемый пользователь не является пациентом?
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        doctorProfileService.attachPatientToDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }

    @DeleteMapping("/attached-patients")
    @PreAuthorize("hasAuthority('ATTACHED_PATIENT_ATTACH_DETACH')")
    public PatientAttachDetachDTO detachPatient(@PathVariable int userId,
                                                @RequestBody @Valid PatientAttachDetachDTO attachDetachDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        doctorProfileService.detachPatientFromDoctor(userId, attachDetachDTO.patientId());
        return attachDetachDTO;
    }
}
