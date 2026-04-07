package com.artlighter.glucosecontrolservice.integration.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import com.artlighter.glucosecontrolservice.integration.repository.IntegrationProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(IntegrationProfileService.class)
public class IntegrationProfileServiceTests {
    @MockitoBean
    private IntegrationProfileRepository integrationProfileRepository;
    @Autowired
    private IntegrationProfileService integrationProfileService;

    @Test
    public void getByPatientProfileId_RepositoryFindsProfile_ReturnsCorrectInsulinProfile() {
        IntegrationProfile expected = new IntegrationProfile(1,true, "api-secret");
        when(integrationProfileRepository.findByPatientProfileId(1)).thenReturn(expected);

        IntegrationProfile actual = integrationProfileService.getByPatientProfileId(1);

        assertEquals(expected, actual);
    }

    @Test
    public void getByPatientProfileId_RepositoryReturnsNull_ThrowsResourceNotFoundException() {
        IntegrationProfile existing = new IntegrationProfile(1, true, "api-secret");
        when(integrationProfileRepository.findByPatientProfileId(1)).thenReturn(existing);

        assertThrows(ResourceNotFoundException.class, () -> integrationProfileService.getByPatientProfileId(2));
        assertThrows(ResourceNotFoundException.class, () -> integrationProfileService.getByPatientProfileId(5));
        assertThrows(ResourceNotFoundException.class, () -> integrationProfileService.getByPatientProfileId(10));
    }

    @Test
    public void createIntegrationProfile_ProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                integrationProfileService.createIntegrationProfile(null, 3));
    }

    @Test
    public void createIntegrationProfile_ProfileAlreadyExists_ThrowsResourceAlreadyExistsException() {
        when(integrationProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                integrationProfileService
                        .createIntegrationProfile(new IntegrationProfile(1, true, "api-secret"), 1));
    }

    @Test
    public void createIntegrationProfile_CallsRepositoryToSaveIntegrationProfileAndReturnsSaved() {
        //ID = 0, чтобы проверить, что сервис выставляет ID из аргументов метода независимо от ID, стоящего во входном
        //профиле
        IntegrationProfile expected = new IntegrationProfile(0, true, "api-secret");
        when(integrationProfileRepository.save(expected)).thenReturn(expected);

        IntegrationProfile actual = integrationProfileService.createIntegrationProfile(expected, 1);

        verify(integrationProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(1, actual.getPatientProfileId());
    }

    @Test
    public void updateIntegrationProfile_ProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                integrationProfileService.updateIntegrationProfile(null, 3));
    }

    @Test
    public void updateIntegrationProfile_ProfileDoesNotExist_ThrowsResourceNotFoundException() {
        when(integrationProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () ->
                integrationProfileService.updateIntegrationProfile(new IntegrationProfile(), 2));
    }

    @Test
    public void updateIntegrationProfile_CallsRepositoryToSaveIntegrationProfileAndReturnsUpdated() {
        IntegrationProfile expected
                = new IntegrationProfile(0, true, "api-secret");
        when(integrationProfileRepository.save(expected)).thenReturn(expected);
        when(integrationProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        IntegrationProfile actual = integrationProfileService.updateIntegrationProfile(expected, 1);

        verify(integrationProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(1, actual.getPatientProfileId());
    }

}
