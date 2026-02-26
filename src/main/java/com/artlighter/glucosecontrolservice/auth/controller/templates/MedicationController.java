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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

//    @Override
//    @GetMapping("/medication-by-name")
//    public MedicationDTO getTemplate(int userId, String name) {
//        return super.getTemplate(userId, name);
//    }

    @Override
    @Operation(summary = "Получить список заготовленных препаратов пользователя.", description = "Возвращает список " +
            "постранично с возможностью сортировки по определенному полю. Доступ имеет только владелец списка.")
    @GetMapping("/medications")
    public Page<MedicationDTO> getTemplates(int userId, Pageable pageable) {
        return super.getTemplates(userId, pageable);
    }

    @Override
    @Operation(summary = "Получить список заготовленных препаратов пользователя с поиском по названию.",
            description = "Возвращает список постранично с возможностью сортировки по определенному полю. " +
                    " Доступ имеет только владелец списка.")
    @GetMapping("/medications/search")
    public Page<MedicationDTO> getTemplatesBySearchQuery(int userId, String query, Pageable pageable) {
        return super.getTemplatesBySearchQuery(userId, query, pageable);
    }

    @Override
    @Operation(summary = "Добавить препарат для пользователя.")
    @PostMapping("/medications")
    public MedicationDTO postTemplate(int userId, MedicationDTO template, BindingResult bindingResult) {
        return super.postTemplate(userId, template, bindingResult);
    }

    @Override
    @Operation(summary = "Обновить существующий препарат для пользователя.")
    @PutMapping("/medications")
    public MedicationDTO putTemplate(int userId, MedicationDTO template, BindingResult bindingResult) {
        return super.putTemplate(userId, template, bindingResult);
    }

    @Override
    @Operation(summary = "Удалить существующий препарат для пользователя.")
    @DeleteMapping("/medications")
    public void deleteTemplate(int userId,
                               @Parameter(required = true, description = "Наименование препарата") String name) {
        super.deleteTemplate(userId, name);
    }

    @Operation(summary = "Рассчитать общее количество миллиграмм дозировки препаратов " +
            "на основе названий переданных препаратов и их порций.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "В случае успеха.")})
    @PostMapping("/medications/calculate")
    public MedicationResult calculateMilligrams(@PathVariable int userId, @RequestBody Map<String, Integer> portions) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);

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
