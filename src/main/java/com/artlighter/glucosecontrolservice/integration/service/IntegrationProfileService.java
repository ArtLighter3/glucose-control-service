package com.artlighter.glucosecontrolservice.integration.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import com.artlighter.glucosecontrolservice.integration.repository.IntegrationProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для доступа и модификации профилей интеграции IntegrationProfile.
 */

@Service
@Transactional
public class IntegrationProfileService {
    private IntegrationProfileRepository integrationProfileRepository;

    public IntegrationProfileService(IntegrationProfileRepository integrationProfileRepository) {
        this.integrationProfileRepository = integrationProfileRepository;
    }

    /**
     * Находит профиль интеграций по ID профиля больного.
     * @param patientProfileId - ID профиля больного
     * @return профиль интеграций IntegrationProfile; никогда не null;
     * @throws ResourceNotFoundException если профиль не был найден;
     */
    @Transactional(readOnly = true)
    public IntegrationProfile getByPatientProfileId(int patientProfileId) {
        IntegrationProfile integrationProfile = integrationProfileRepository.findByPatientProfileId(patientProfileId);
        if (integrationProfile == null)
            throw new ResourceNotFoundException(IntegrationProfile.class,
                    "integration profile for patient profile with ID '" + patientProfileId + "' not found");

        return integrationProfile;
    }

    /**
     * Создает профиль интеграций для больного.
     * @param integrationProfile создаваемый профиль интеграций, не null;
     * @param patientProfileId ID профиля больного;
     * @return созданный IntegrationProfile;
     * @throws IllegalArgumentException если integrationProfile равен null;
     * @throws ResourceAlreadyExistsException если профиль для больного уже существует;
     */
    public IntegrationProfile createIntegrationProfile(IntegrationProfile integrationProfile, int patientProfileId) {
        if (integrationProfile == null) throw new IllegalArgumentException("integrationProfile cannot be null");

        if (integrationProfileRepository.existsByPatientProfileId(patientProfileId))
            throw new ResourceAlreadyExistsException(integrationProfile,
                    "integration profile for patient profile with ID '" + patientProfileId + "' already exists");

        integrationProfile.setPatientProfileId(patientProfileId);
        return integrationProfileRepository.save(integrationProfile);
    }

    /**
     * Обновляет профиль интеграций для больного.
     * @param integrationProfile создаваемый профиль интеграций, не null;
     * @param patientProfileId ID профиля больного;
     * @return созданный IntegrationProfile;
     * @throws IllegalArgumentException если integrationProfile равен null;
     * @throws ResourceNotFoundException если профиль интеграций для больного не существует;
     */
    public IntegrationProfile updateIntegrationProfile(IntegrationProfile integrationProfile, int patientProfileId) {
        if (integrationProfile == null) throw new IllegalArgumentException("integrationProfile cannot be null");

        if (!integrationProfileRepository.existsByPatientProfileId(patientProfileId))
            throw new ResourceNotFoundException(IntegrationProfile.class,
                    "integration profile for patient profile with ID '" + patientProfileId + "' not found");

        integrationProfile.setPatientProfileId(patientProfileId);
        return integrationProfileRepository.save(integrationProfile);
    }

}
