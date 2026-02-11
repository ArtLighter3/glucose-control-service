package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import com.artlighter.glucosecontrolservice.user.repository.PatientProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientProfileService {
    private PatientProfileRepository patientProfileRepository;

    public PatientProfileService(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    @Transactional(readOnly = true)
    public PatientProfile getByUserId(int userId) {
        //TODO а что если по каким-то причинам у больного не создался его профиль?
        return patientProfileRepository.getPatientProfileByUserId(userId);
    }

    public PatientProfile createProfileForPatient(PatientProfile patientProfile, int userId) {
        if (patientProfileRepository.existsByUserId(userId))
            throw new ResourceAlreadyExistsException(patientProfile, "Patient profile for this user already exists");

        patientProfile.setUserId(userId);
        return patientProfileRepository.save(patientProfile);
    }

    public PatientProfile createDefaultProfileForPatient(int userId) {
        PatientProfile patientProfile = null;

        if (!patientProfileRepository.existsByUserId(userId)) {
            patientProfile = new PatientProfile();
            patientProfile.setUserId(userId);
            patientProfile.setCarbsUnit(CarbsUnit.GRAMS);
            patientProfile.setDiabetesType(1);
            patientProfile.setGlucoseUnit(GlucoseUnit.MILLIMOLES_PER_LITER);

            patientProfileRepository.save(patientProfile);
        }

        return patientProfile;
    }

    public PatientProfile updateProfileForPatient(PatientProfile patientProfile, int userId) {
        PatientProfile existingProfile = patientProfileRepository.getPatientProfileByUserId(userId);
        if (existingProfile == null)
            throw new ResourceNotFoundException("Patient profile for this user does not exist");

        patientProfile.setUserId(userId);
        patientProfile.setId(existingProfile.getId());
        return patientProfileRepository.save(patientProfile);
    }

    public void deletePatientProfile(int userId) {
        patientProfileRepository.deleteByUserId(userId);
    }

//    private void checkArguments(PatientProfile patientProfile) {
//        if (patientProfile == null) throw new IllegalArgumentException("patientProfile cannot be null");
//    }
}
