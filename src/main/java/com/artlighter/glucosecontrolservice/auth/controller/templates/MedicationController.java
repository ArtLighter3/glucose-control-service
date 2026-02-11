package com.artlighter.glucosecontrolservice.auth.controller.templates;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.templates.dto.MedicationDTO;
import com.artlighter.glucosecontrolservice.templates.dto.MedicationResult;
import com.artlighter.glucosecontrolservice.templates.dto.TemplateDeletionDTO;
import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import com.artlighter.glucosecontrolservice.templates.service.impl.MedicationService;
import com.artlighter.glucosecontrolservice.templates.util.mapper.MedicationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MedicationController extends AbstractPatientTemplateResourceController<Medication, MedicationDTO>{
    private MedicationService medicationService;
    private PatientProfileService patientProfileService;
    private MedicationMapper medicationMapper;

    public MedicationController(MedicationService medicationService,
                                PatientProfileService patientProfileService,
                                MedicationMapper medicationMapper) {
        this.medicationService = medicationService;
        this.patientProfileService = patientProfileService;
        this.medicationMapper = medicationMapper;
    }

    @Override
    @GetMapping("/medication-by-name")
    public MedicationDTO getTemplate(int userId, String name) {
        return super.getTemplate(userId, name);
    }

    @Override
    @GetMapping("/medications")
    public Page<MedicationDTO> getTemplates(int userId, Pageable pageable) {
        return super.getTemplates(userId, pageable);
    }

    @Override
    @GetMapping("/medications/search")
    public Page<MedicationDTO> getTemplatesBySearchQuery(int userId, String query, Pageable pageable) {
        return super.getTemplatesBySearchQuery(userId, query, pageable);
    }

    @Override
    @PostMapping("/medications")
    public MedicationDTO postTemplate(int userId, MedicationDTO template, BindingResult bindingResult) {
        return super.postTemplate(userId, template, bindingResult);
    }

    @Override
    @PutMapping("/medications")
    public MedicationDTO putTemplate(int userId, MedicationDTO template, BindingResult bindingResult) {
        return super.putTemplate(userId, template, bindingResult);
    }

    @Override
    @DeleteMapping("/medications")
    public TemplateDeletionDTO deleteTemplate(int userId, TemplateDeletionDTO deletionDTO,
                                 BindingResult bindingResult) {
        return super.deleteTemplate(userId, deletionDTO, bindingResult);
    }

    @PostMapping("/medications/calculate")
    public MedicationResult calculateMilligrams(@PathVariable int userId, @RequestBody Map<String, Integer> portions) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        float overallDose = getTemplateService().calculateOverallMilligrams(patientProfile.getId(), portions);
        return new MedicationResult(overallDose);
    }


    @Override
    protected MedicationService getTemplateService() {
        return medicationService;
    }

    @Override
    protected PatientProfileService getPatientProfileService() {
        return patientProfileService;
    }

    @Override
    protected MedicationMapper getTemplateMapper() {
        return medicationMapper;
    }
}
