package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.repository.InsulinProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsulinProfileService {
    private InsulinProfileRepository insulinProfileRepository;
    //private PatientProfileService patientProfileService;

    public InsulinProfileService(InsulinProfileRepository insulinProfileRepository/*,
                                 PatientProfileService patientProfileService*/) {
        this.insulinProfileRepository = insulinProfileRepository;
        //this.patientProfileService = patientProfileService;
    }

    @Transactional(readOnly = true)
    public InsulinProfile getByPatientProfileId(int patientProfileId) {
        return insulinProfileRepository.getByProfileId(patientProfileId);
    }

//    @Transactional(readOnly = true)
//    public InsulinProfile getByUserId(int userId) {
//        Integer profileId = patientProfileService.getProfileId(userId);
//        if (profileId == null) return null;
//
//        return getByPatientProfileId(profileId);
//    }

    public InsulinProfile createInsulinProfile(InsulinProfile insulinProfile, int patientProfileId) {
        if (insulinProfileRepository.existsByProfileId(patientProfileId))
            throw new ResourceAlreadyExistsException(insulinProfile, "Insulin profile for this user already exists");

        insulinProfile.setProfileId(patientProfileId);
        return insulinProfileRepository.save(insulinProfile);
    }

    public InsulinProfile updateInsulinProfile(InsulinProfile insulinProfile, int patientProfileId) {
        if (!insulinProfileRepository.existsByProfileId(patientProfileId))
            throw new ResourceNotFoundException("Insulin profile for this user does not exist");

        insulinProfile.setProfileId(patientProfileId);
        return insulinProfileRepository.save(insulinProfile);
    }

    public void deleteInsulinProfile(int patientProfileId) {
        insulinProfileRepository.deleteByProfileId(patientProfileId);
    }

}
