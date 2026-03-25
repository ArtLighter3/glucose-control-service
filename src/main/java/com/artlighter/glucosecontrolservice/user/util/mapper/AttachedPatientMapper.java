package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.AttachedPatientDTO;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.stereotype.Component;

@Component
public class AttachedPatientMapper implements DTOMapper<PatientProfile, AttachedPatientDTO> {
    @Override
    public AttachedPatientDTO mapToDTO(PatientProfile internal) {
        return new AttachedPatientDTO(internal.getUser().getUsername(), internal.getUser().getUsername(),
                internal.getUser().getUsername(), internal.getUser().getId(), internal.getUserId(),
                null);
    }

    @Override
    public PatientProfile mapToInternal(AttachedPatientDTO externalDTO) {
        return null;
    }
}
