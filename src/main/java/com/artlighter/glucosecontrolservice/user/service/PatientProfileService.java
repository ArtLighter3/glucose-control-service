package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.entity.CarbsUnit;
import com.artlighter.glucosecontrolservice.user.entity.GlucoseUnit;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.repository.PatientProfileRepository;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return patientProfileRepository.getByUserId(userId);
    }

    @Transactional(readOnly = true)
    public PatientProfile getByUsername(String username) {
        return patientProfileRepository.getByUserUsername(username);
    }

    public PatientProfile createProfileForPatient(PatientProfile patientProfile, User user) {
        if (patientProfileRepository.existsByUserId(user.getId()))
            throw new ResourceAlreadyExistsException(patientProfile, "Patient profile for this user already exists");

        //patientProfile.setUserId(userId);
        patientProfile.setUser(user);
        return patientProfileRepository.save(patientProfile);
    }

    public PatientProfile createDefaultProfileForPatient(User user) {
        PatientProfile patientProfile = null;

        if (!patientProfileRepository.existsByUserId(user.getId())) {
            patientProfile = new PatientProfile();
           // patientProfile.setUserId(userId);
            patientProfile.setUser(user);
            patientProfile.setCarbsUnit(CarbsUnit.GRAMS);
            patientProfile.setDiabetesType(1);
            patientProfile.setGlucoseUnit(GlucoseUnit.MILLIMOLES_PER_LITER);

            patientProfileRepository.save(patientProfile);
        }

        return patientProfile;
    }

    public PatientProfile updateProfileForPatient(PatientProfile patientProfile, int userId) {
        PatientProfile existingProfile = patientProfileRepository.getByUserId(userId);
        if (existingProfile == null)
            throw new ResourceNotFoundException("Patient profile for this user does not exist");

        //patientProfile.setUserId(userId);
        patientProfile.setUser(new User(userId));
        patientProfile.setId(existingProfile.getId());
        return patientProfileRepository.save(patientProfile);
    }

    public Page<PatientProfile> getPatientsAttachedToDoctor(int doctorProfileId, Pageable pageable) {
        return getPatientsAttachedToDoctor(doctorProfileId, null, pageable);
    }

    public Page<PatientProfile> getPatientsAttachedToDoctor(int doctorProfileId, @Nullable String searchQuery,
                                                            Pageable pageable) {
        if (pageable == null)
            pageable = PageRequest.of(0, 10, Sort.by("user.username"));
        if (pageable.getPageSize() > 20)
            pageable = PageRequest.of(pageable.getPageNumber(), 20, pageable.getSort());

        Page<PatientProfile> patientProfiles = searchQuery == null ?
                patientProfileRepository.getPatientsAttachedToDoctorByDoctorId(doctorProfileId, pageable) :
                patientProfileRepository.searchPatientsAttachedToDoctorByDoctorId(doctorProfileId,
                        "%" + searchQuery + "%", pageable);
        if (patientProfiles == null) return Page.empty(pageable);

        return patientProfiles;
    }

    public String getNightscoutApiSecretIfEnabled(String patientUsername) {
        PatientProfile patientProfile = patientProfileRepository.getByUserUsername(patientUsername);
        if (patientProfile == null || !patientProfile.isNightscoutEnabled()) return null;

        return patientProfile.getNightscoutApiSecret();
    }

    public void deletePatientProfile(int userId) {
        patientProfileRepository.deleteByUserId(userId);
    }

//    private void checkArguments(PatientProfile patientProfile) {
//        if (patientProfile == null) throw new IllegalArgumentException("patientProfile cannot be null");
//    }
}
