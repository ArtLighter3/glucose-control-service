package com.artlighter.glucosecontrolservice.templates.util.mapper;

import com.artlighter.glucosecontrolservice.templates.dto.MedicationDTO;
import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper implements TemplateMapper<Medication, MedicationDTO> {
    @Override
    public MedicationDTO mapToDTO(Medication internal) {
        return new MedicationDTO(internal.getId().getName(),
                internal.getMilligramsInPortion(), internal.getDefaultPortions());
    }

    @Override
    public Medication mapToInternal(MedicationDTO externalDTO) {
        return new Medication(new PatientTemplateEntity.PatientTemplateEntityID(0, externalDTO.name()),
                externalDTO.milligramsInPortion(), externalDTO.defaultPortions());
    }
}
