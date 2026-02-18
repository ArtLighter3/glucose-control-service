package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.user.dto.PatientProfileDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import org.springframework.stereotype.Component;

@Component
public class PatientProfileMapper implements DTOMapper<PatientProfile, PatientProfileDTO> {
    @Override
    public PatientProfileDTO mapToDTO(PatientProfile internal) {
        return new PatientProfileDTO(internal.getGlucoseUnit(), internal.getCarbsUnit(), internal.getDiabetesType(),
                internal.getHyperGlucose(), internal.getHighGlucose(),
                internal.getLowGlucose(), internal.getHypoGlucose(),
                internal.isNightscoutEnabled(), internal.getNightscoutApiSecret());
    }

    @Override
    public PatientProfile mapToInternal(PatientProfileDTO externalDTO) {
        PatientProfile patientProfile = new PatientProfile();

        patientProfile.setGlucoseUnit(externalDTO.glucoseUnit());
        patientProfile.setCarbsUnit(externalDTO.carbsUnit());
        patientProfile.setDiabetesType(externalDTO.diabetesType());
        patientProfile.setHyperGlucose(externalDTO.hyperGlucose());
        patientProfile.setHighGlucose(externalDTO.highGlucose());
        patientProfile.setLowGlucose(externalDTO.lowGlucose());
        patientProfile.setHypoGlucose(externalDTO.hypoGlucose());
        patientProfile.setNightscoutEnabled(externalDTO.isNightscoutEnabled());
        patientProfile.setNightscoutApiSecret(externalDTO.nightscoutApiSecret());

        return patientProfile;
    }
}
