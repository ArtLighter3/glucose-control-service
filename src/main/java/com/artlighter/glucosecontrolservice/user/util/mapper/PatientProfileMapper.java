package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.user.dto.PatientProfileDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class PatientProfileMapper implements DTOMapper<PatientProfile, PatientProfileDTO> {
    private DecimalFormat decimalOutputFormat;

    public PatientProfileMapper(DecimalFormat decimalOutputFormat) {
        this.decimalOutputFormat = decimalOutputFormat;
    }

    @Override
    public PatientProfileDTO mapToDTO(PatientProfile internal) {
        return new PatientProfileDTO(internal.getGlucoseUnit(), internal.getCarbsUnit(), internal.getDiabetesType(),
                Float.valueOf(decimalOutputFormat
                        .format(internal.getGlucoseUnit().convertFromMmolPerLiter(internal.getHyperGlucose()))),
                Float.valueOf(decimalOutputFormat
                        .format(internal.getGlucoseUnit().convertFromMmolPerLiter(internal.getHighGlucose()))),
                Float.valueOf(decimalOutputFormat
                        .format(internal.getGlucoseUnit().convertFromMmolPerLiter(internal.getLowGlucose()))),
                Float.valueOf(decimalOutputFormat
                        .format(internal.getGlucoseUnit().convertFromMmolPerLiter(internal.getHypoGlucose()))),
                internal.isNightscoutEnabled(), internal.getNightscoutApiSecret());
    }

    @Override
    public PatientProfile mapToInternal(PatientProfileDTO externalDTO) {
        PatientProfile patientProfile = new PatientProfile();

        patientProfile.setGlucoseUnit(externalDTO.glucoseUnit());
        patientProfile.setCarbsUnit(externalDTO.carbsUnit());
        patientProfile.setDiabetesType(externalDTO.diabetesType());

        patientProfile
                .setHyperGlucose((float)externalDTO.glucoseUnit().convertToMmolPerLiter(externalDTO.hyperGlucose()));
        patientProfile.setHighGlucose((float)externalDTO.glucoseUnit().convertToMmolPerLiter(externalDTO.highGlucose()));
        patientProfile.setLowGlucose((float)externalDTO.glucoseUnit().convertToMmolPerLiter(externalDTO.lowGlucose()));
        patientProfile.setHypoGlucose((float)externalDTO.glucoseUnit().convertToMmolPerLiter(externalDTO.hypoGlucose()));

        patientProfile.setNightscoutEnabled(externalDTO.isNightscoutEnabled());
        patientProfile.setNightscoutApiSecret(externalDTO.nightscoutApiSecret());

        return patientProfile;
    }
}
