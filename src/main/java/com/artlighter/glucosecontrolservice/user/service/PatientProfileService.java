package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.*;
import com.artlighter.glucosecontrolservice.user.repository.PatientProfileRepository;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для доступа и модификации профилей больных.
 */

@Service
@Transactional
public class PatientProfileService {
    private PatientProfileRepository patientProfileRepository;

    public PatientProfileService(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    /**
     * Находит профиль больного по ID этого больного.
     * @param patientId ID больного.
     * @return PatientProfile, соответствующий ID больного; null, если не существует;
     */
    @Transactional(readOnly = true)
    public PatientProfile getByUserId(int patientId) {
        //TODO а что если по каким-то причинам у больного не создался его профиль?
        return patientProfileRepository.findByUserId(patientId);
    }

    /**
     * Находит профиль больного по имени пользователя этого больного.
     * @param username имя пользователя больного.
     * @return PatientProfile, соответствующий имени пользователя; null, если не существует;
     */
    @Transactional(readOnly = true)
    public PatientProfile getByUsername(String username) {
        return patientProfileRepository.findByUserUsername(username);
    }

    /**
     * Создает профиль больного для пользователя.
     * @param patientProfile сохраняемый профиль больного;
     * @param user пользователь, для которого нужно сохранить профиль больного;
     * @return сохраненный PatientProfile;
     * @throws ResourceAlreadyExistsException если профиль больного уже существует для этого пользователя.
     * @throws IllegalArgumentException если patientProfile или user равны null;
     * @throws UserIsNotPatientException если пользователь не является больным;
     */
    public PatientProfile createProfileForPatient(PatientProfile patientProfile, User user) {
        if (patientProfile == null) throw new IllegalArgumentException("PatientProfile cannot be null");
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        if (patientProfileRepository.existsByUserId(user.getId()))
            throw new ResourceAlreadyExistsException(patientProfile, "patient profile for this user already exists");
        if (!user.getRoles().contains(Role.ROLE_PATIENT))
            throw new UserIsNotPatientException(user);

        //patientProfile.setUserId(userId);
        patientProfile.setUser(user);
        return patientProfileRepository.save(patientProfile);
    }

    /**
     * Создает профиль больного для пользователя с настройками по-умолчанию.
     * @param user пользователь, для которого нужно создать профиль больного;
     * @return сохраненный PatientProfile;
     * @throws ResourceAlreadyExistsException если профиль больного уже существует для этого пользователя.
     * @throws IllegalArgumentException если user равен null;
     * @throws UserIsNotPatientException если пользователь не является больным;
     */
    public PatientProfile createDefaultProfileForPatient(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");

        PatientProfile patientProfile = null;
        if (patientProfileRepository.existsByUserId(user.getId()))
            throw new ResourceAlreadyExistsException(patientProfile, "patient profile for this user already exists");
        if (!user.getRoles().contains(Role.ROLE_PATIENT))
            throw new UserIsNotPatientException(user);

        patientProfile = new PatientProfile();

        patientProfile.setUser(user);
        patientProfile.setCarbsUnit(CarbsUnit.GRAMS);
        patientProfile.setDiabetesType(1);patientProfile.setGlucoseUnit(GlucoseUnit.MILLIMOLES_PER_LITER);

        return patientProfileRepository.save(patientProfile);
    }

    public PatientProfile updateProfileForPatient(PatientProfile patientProfile, int userId) {
        PatientProfile existingProfile = patientProfileRepository.findByUserId(userId);
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
        PatientProfile patientProfile = patientProfileRepository.findByUserUsername(patientUsername);
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
