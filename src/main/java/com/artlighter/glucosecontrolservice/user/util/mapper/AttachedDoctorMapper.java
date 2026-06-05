package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.AttachedDoctorDTO;
import com.artlighter.glucosecontrolservice.user.dto.AttachedPatientDTO;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.stereotype.Component;

@Component
public class AttachedDoctorMapper implements DTOMapper<DoctorProfile, AttachedDoctorDTO> {
    @Override
    public AttachedDoctorDTO mapToDTO(DoctorProfile internal) {
        return new AttachedDoctorDTO(
                internal.getUser().getLastName(),
                internal.getUser().getFirstName(),
                internal.getUser().getMiddleName(),
                internal.getPersonalSecret()
        );
    }

    @Override
    public DoctorProfile mapToInternal(AttachedDoctorDTO externalDTO) {
        throw new UnsupportedOperationException();
    }
}
