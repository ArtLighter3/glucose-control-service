package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.repository.InsulinProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для доступа и модификации инсулиновых профилей InsulinProfile.
 */

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

    /**
     * Находит инсулиновый профиль по ID профиля больного (не по ID самого больного!).
     * @param patientProfileId - ID профиля больного
     * @return InsulinProfile; null, если не найден;
     */
    @Transactional(readOnly = true)
    public InsulinProfile getByPatientProfileId(int patientProfileId) {
        return insulinProfileRepository.findByProfileId(patientProfileId);
    }

//    @Transactional(readOnly = true)
//    public InsulinProfile getByUserId(int userId) {
//        Integer profileId = patientProfileService.getProfileId(userId);
//        if (profileId == null) return null;
//
//        return getByPatientProfileId(profileId);
//    }

    /**
     * Создает инсулиновый профиль для больного.
     * @param insulinProfile создаваемый инсулиновый профиль, не null;
     * @param patientProfileId ID профиля больного (не ID самого больного!);
     * @return созданный InsulinProfile;
     * @throws IllegalArgumentException если insulinProfile равен null;
     * @throws ResourceAlreadyExistsException если инсулиновый профиль для больного уже существует;
     */
    public InsulinProfile createInsulinProfile(InsulinProfile insulinProfile, int patientProfileId) {
        if (insulinProfile == null) throw new IllegalArgumentException("insulinProfile cannot be null");

        if (insulinProfileRepository.existsByProfileId(patientProfileId))
            throw new ResourceAlreadyExistsException(insulinProfile, "insulin profile for this user already exists");

        insulinProfile.setProfileId(patientProfileId);
        return insulinProfileRepository.save(insulinProfile);
    }

    /**
     * Обновляет инсулиновый профиль для больного.
     * @param insulinProfile обновляемый инсулиновый профиль, не null;
     * @param patientProfileId ID профиля больного (не ID самого больного!);
     * @return обновленный InsulinProfile;
     * @throws IllegalArgumentException если insulinProfile равен null;
     * @throws ResourceNotFoundException если инсулиновый профиль для больного не существует;
     */
    public InsulinProfile updateInsulinProfile(InsulinProfile insulinProfile, int patientProfileId) {
        if (insulinProfile == null) throw new IllegalArgumentException("insulinProfile cannot be null");

        if (!insulinProfileRepository.existsByProfileId(patientProfileId))
            throw new ResourceNotFoundException("insulin profile for this user does not exist");

        insulinProfile.setProfileId(patientProfileId);
        return insulinProfileRepository.save(insulinProfile);
    }

//    public void deleteInsulinProfile(int patientProfileId) {
//        insulinProfileRepository.deleteByProfileId(patientProfileId);
//    }

}
