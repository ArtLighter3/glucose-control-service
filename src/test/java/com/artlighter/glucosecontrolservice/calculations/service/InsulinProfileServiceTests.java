package com.artlighter.glucosecontrolservice.calculations.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.calculations.repository.InsulinProfileRepository;
import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(InsulinProfileService.class)
public class InsulinProfileServiceTests {
    @MockitoBean
    private InsulinProfileRepository insulinProfileRepository;
    @Autowired
    private InsulinProfileService insulinProfileService;

    @Test
    public void getByPatientProfileId_RepositoryFindsProfile_ReturnsCorrectInsulinProfile() {
        InsulinProfile expected = new InsulinProfile(1, 30f, 30f, 5, null, null);
        when(insulinProfileRepository.findByPatientProfileId(1)).thenReturn(expected);

        InsulinProfile actual = insulinProfileService.getByPatientProfileId(1);

        assertEquals(expected, actual);
    }

    @Test
    public void getByPatientProfileId_RepositoryReturnsNull_ThrowsResourceNotFoundException() {
        InsulinProfile existing = new InsulinProfile(1, 30f, 30f, 5, null, null);
        when(insulinProfileRepository.findByPatientProfileId(1)).thenReturn(existing);

        assertThrows(ResourceNotFoundException.class, () -> insulinProfileService.getByPatientProfileId(2));
        assertThrows(ResourceNotFoundException.class, () -> insulinProfileService.getByPatientProfileId(5));
        assertThrows(ResourceNotFoundException.class, () -> insulinProfileService.getByPatientProfileId(10));
    }

    @Test
    public void createInsulinProfile_ProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> insulinProfileService.createInsulinProfile(null, 3));
    }

    @Test
    public void createInsulinProfile_ProfileAlreadyExists_ThrowsResourceAlreadyExistsException() {
        when(insulinProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                insulinProfileService.createInsulinProfile(new InsulinProfile(), 1));
    }

    @Test
    public void createInsulinProfile_CallsRepositoryToSaveInsulinProfileAndReturnsSaved() {
        InsulinProfile expected = new InsulinProfile(0, 30f, 30f, 5, null, null);
        when(insulinProfileRepository.save(expected)).thenReturn(expected);

        InsulinProfile actual = insulinProfileService.createInsulinProfile(expected, 1);

        verify(insulinProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(1, actual.getPatientProfileId());
    }

    @Test
    public void updateInsulinProfile_ProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> insulinProfileService.updateInsulinProfile(null, 3));
    }

    @Test
    public void updateInsulinProfile_ProfileDoesNotExist_ThrowsResourceNotFoundException() {
        when(insulinProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () ->
                insulinProfileService.updateInsulinProfile(new InsulinProfile(), 2));
    }

    @Test
    public void updateInsulinProfile_CallsRepositoryToSaveInsulinProfileAndReturnsUpdated() {
        InsulinProfile expected = new InsulinProfile(0, 30f, 30f, 5, null, null);
        when(insulinProfileRepository.save(expected)).thenReturn(expected);
        when(insulinProfileRepository.existsByPatientProfileId(1)).thenReturn(true);

        InsulinProfile actual = insulinProfileService.updateInsulinProfile(expected, 1);

        verify(insulinProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(1, actual.getPatientProfileId());
    }

//    private boolean insulinProfilesEqual(InsulinProfile expected, InsulinProfile actual) {
//        if (expected == actual) return true;
//
//        return false;
//    }
}
