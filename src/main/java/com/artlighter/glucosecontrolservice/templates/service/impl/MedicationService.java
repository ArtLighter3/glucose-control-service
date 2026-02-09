package com.artlighter.glucosecontrolservice.templates.service.impl;

import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.repository.MedicationRepository;
import com.artlighter.glucosecontrolservice.templates.service.TemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MedicationService extends TemplateService<Medication> {

    public MedicationService(MedicationRepository medicationRepository) {
        super(medicationRepository);
    }

    @Transactional(readOnly = true)
    public float calculateOverallMilligrams(int patientProfileId, Map<String, Integer> portions) {
        if (portions == null || portions.isEmpty()) return 0f;

        List<PatientTemplateEntity.PatientTemplateEntityID> ids = new ArrayList<>();
        for (String medName : portions.keySet())
            ids.add(new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, medName));

        List<Medication> medications = patientTemplateEntityRepository.findAllById(ids);

        float result = 0f;
        for (Medication med : medications)
            result += portions.getOrDefault(med.getId().getName(), 1) * med.getMilligramsInPortion();

        return result;
    }
}
