package com.artlighter.glucosecontrolservice.auth.controller.templates;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.templates.dto.CarbsResult;
import com.artlighter.glucosecontrolservice.templates.dto.MealDTO;
import com.artlighter.glucosecontrolservice.templates.dto.TemplateDeletionDTO;
import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.service.impl.MealService;
import com.artlighter.glucosecontrolservice.templates.util.mapper.MealMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MealController extends AbstractPatientTemplateResourceController<Meal, MealDTO>{
    private MealService mealService;
    private PatientProfileService patientProfileService;
    private MealMapper mealMapper;

    public MealController(MealService mealService,
                          PatientProfileService patientProfileService,
                          MealMapper mealMapper) {
        this.mealService = mealService;
        this.patientProfileService = patientProfileService;
        this.mealMapper = mealMapper;
    }

    @Override
    @GetMapping("/meal-by-name")
    public MealDTO getTemplate(int userId, String name) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Meal meal = getTemplateService().getByName(patientProfile.getId(), name);

        return getTemplateMapper().mapToDtoWithUnitConversion(meal, patientProfile.getCarbsUnit());
    }

    @Override
    @GetMapping("/meals")
    public Page<MealDTO> getTemplates(int userId, Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<Meal> meals = getTemplateService().getAllByPatientProfileId(patientProfile.getId(), pageable);

        return meals.map((meal) ->
                getTemplateMapper().mapToDtoWithUnitConversion(meal, patientProfile.getCarbsUnit()));
    }

    @Override
    @GetMapping("/meals/search")
    public Page<MealDTO> getTemplatesBySearchQuery(int userId, String query, Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<Meal> meals = getTemplateService().searchByNameQuery(patientProfile.getId(), query, pageable);

        return meals.map((meal) ->
                getTemplateMapper().mapToDtoWithUnitConversion(meal, patientProfile.getCarbsUnit()));
    }

    @Override
    @PostMapping("/meals")
    public MealDTO postTemplate(int userId, MealDTO template, BindingResult bindingResult) {
        return super.postTemplate(userId, template, bindingResult);
    }

    @Override
    @PutMapping("/meals")
    public MealDTO putTemplate(int userId, MealDTO template, BindingResult bindingResult) {
        return super.putTemplate(userId, template, bindingResult);
    }

    @Override
    @DeleteMapping("/meals")
    public TemplateDeletionDTO deleteTemplate(int userId, TemplateDeletionDTO deletionDTO,
                                              BindingResult bindingResult) {
        return super.deleteTemplate(userId, deletionDTO, bindingResult);
    }

    @PostMapping("/meals/calculate")
    public CarbsResult calculateCarbs(@PathVariable int userId, @RequestBody Map<String, Float> mealWeights) {
        PatientProfile patientProfile = patientProfileService.getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        float overallCarbs = getTemplateService().calculateOverallCarbs(patientProfile.getId(), mealWeights);
        return new CarbsResult(patientProfile.getCarbsUnit().convertFromGrams(overallCarbs),
                patientProfile.getCarbsUnit());
    }


    @Override
    protected MealService getTemplateService() {
        return mealService;
    }

    @Override
    protected PatientProfileService getPatientProfileService() {
        return patientProfileService;
    }

    @Override
    protected MealMapper getTemplateMapper() {
        return mealMapper;
    }
}
