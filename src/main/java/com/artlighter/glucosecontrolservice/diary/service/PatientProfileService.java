package com.artlighter.glucosecontrolservice.diary.service;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.repository.PatientProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientProfileService {
    private PatientProfileRepository patientProfileRepository;

    public PatientProfileService(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    public PatientProfile getByUserId(int userId) {
        return patientProfileRepository.getPatientProfileByUserId(userId);
    }
}
