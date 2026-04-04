package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.*;
import com.artlighter.glucosecontrolservice.user.repository.PatientProfileRepository;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(PatientProfileService.class)
public class PatientProfileServiceTests {
    @MockitoBean
    private PatientProfileRepository patientProfileRepository;
    @Autowired
    private PatientProfileService patientProfileService;

    @Test
    public void getByUserId_RepositoryFindsProfile_ReturnsCorrectPatientProfile() {
        PatientProfile expected = new PatientProfile(0, new User(),
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 12f, 8f, 4f, false, null);
        when(patientProfileRepository.findByUserId(0)).thenReturn(expected);

        PatientProfile actual = patientProfileService.getByUserId(0);

        assertEquals(expected, actual);
    }

    @Test
    public void getByUserId_RepositoryReturnsNull_ThrowsResourceNotFoundException() {
        PatientProfile existing = new PatientProfile(0, new User(),
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 12f, 8f, 4f, false, null);
        when(patientProfileRepository.findByUserId(0)).thenReturn(existing);

        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUserId(2));
        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUserId(5));
        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUserId(10));
    }

    @Test
    public void getByUsername_RepositoryFindsProfile_ReturnsCorrectPatientProfile() {
        User user = new User();
        user.setUsername("username1");
        PatientProfile expected = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 12f, 8f, 4f, false, null);
        when(patientProfileRepository.findByUserUsername(user.getUsername())).thenReturn(expected);

        PatientProfile actual = patientProfileService.getByUsername("username1");

        assertEquals(expected, actual);
    }

    @Test
    public void getByUsername_RepositoryReturnsNull_ThrowsResourceNotFoundException() {
        User user = new User();
        user.setUsername("username1");
        PatientProfile existing = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 12f, 8f, 4f, false, null);
        when(patientProfileRepository.findByUserUsername(user.getUsername())).thenReturn(existing);

        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUsername("username2"));
        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUsername("username3"));
        assertThrows(ResourceNotFoundException.class, () -> patientProfileService.getByUsername("username4"));
    }

    @Test
    public void createProfileForPatient_ProfileOrUserIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patientProfileService.createProfileForPatient(null, new User()));
        assertThrows(IllegalArgumentException.class,
                () -> patientProfileService.createProfileForPatient(new PatientProfile(), null));
        assertThrows(IllegalArgumentException.class,
                () -> patientProfileService.createProfileForPatient(null, null));
    }

    @Test
    public void createProfileForPatient_ProfileAlreadyExists_ThrowsResourceAlreadyExistsException() {
        when(patientProfileRepository.existsByUserId(1)).thenReturn(true);
        User user = new User();
        user.setId(1);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                patientProfileService.createProfileForPatient(new PatientProfile(), user));
    }

    @Test
    public void createProfileForPatient_UserIsNotPatient_ThrowsUserIsNotPatientException() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_DOCTOR));

        assertThrows(UserIsNotPatientException.class, () ->
                patientProfileService.createProfileForPatient(new PatientProfile(), user));
    }

    @Test
    public void createProfileForPatient_CallsRepositoryToSavePatientProfileAndReturnsSaved() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_PATIENT));
        PatientProfile expected = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 12f, 8f, 4f, false, null);
        when(patientProfileRepository.save(expected)).thenReturn(expected);

        PatientProfile actual = patientProfileService.createProfileForPatient(expected, user);

        verify(patientProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(user.getId(), actual.getUserId());
        assertEquals(user, actual.getUser());
    }

    @Test
    public void createDefaultProfileForPatient_UserIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> patientProfileService.createDefaultProfileForPatient(null));
    }

    @Test
    public void createDefaultProfileForPatient_ProfileAlreadyExists_ThrowsResourceAlreadyExistsException() {
        when(patientProfileRepository.existsByUserId(1)).thenReturn(true);
        User user = new User();
        user.setId(1);

        assertThrows(ResourceAlreadyExistsException.class, () ->
                patientProfileService.createDefaultProfileForPatient(user));
    }

    @Test
    public void createDefaultProfileForPatient_UserIsNotPatient_ThrowsUserIsNotPatientException() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_DOCTOR));

        assertThrows(UserIsNotPatientException.class, () ->
                patientProfileService.createDefaultProfileForPatient(user));
    }

    @Test
    public void createDefaultProfileForPatient_CallsRepositoryToSavePatientProfileAndReturnsCorrectDefaultProfile() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_PATIENT));
        PatientProfile expected = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 8f, 4f, 2f, false, null);
        when(patientProfileRepository.save(any())).thenReturn(expected);

        PatientProfile actual = patientProfileService.createDefaultProfileForPatient(user);

        verify(patientProfileRepository).save(any());
        //assertEquals(expected, actual);
        assertEquals(user.getId(), actual.getUserId());
        assertEquals(user, actual.getUser());
        assertEquals(expected.getCarbsUnit(), actual.getCarbsUnit());
        assertEquals(expected.getGlucoseUnit(), actual.getGlucoseUnit());
        assertEquals(expected.getHighGlucose(), actual.getHighGlucose());
        assertEquals(expected.getLowGlucose(), actual.getLowGlucose());
        assertEquals(expected.getDiabetesType(), actual.getDiabetesType());
        assertEquals(expected.getHyperGlucose(), actual.getHyperGlucose());
        assertEquals(expected.getHypoGlucose(), actual.getHypoGlucose());
    }

    @Test
    public void updateProfileForPatient_ProfileIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> patientProfileService.updateProfileForPatient(null, 3));
    }

    @Test
    public void updateProfileForPatient_ProfileDoesNotExist_ThrowsResourceNotFoundException() {
        when(patientProfileRepository.existsByUserId(1)).thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () ->
                patientProfileService.updateProfileForPatient(new PatientProfile(), 2));
    }

    @Test
    public void updateProfileForPatient_CallsRepositoryToUpdatePatientProfileAndReturnsUpdated() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_PATIENT));
        PatientProfile expected = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIMOLES_PER_LITER, CarbsUnit.GRAMS, 1, 15f, 8f, 4f, 2f, false, null);
        PatientProfile existing = new PatientProfile(user.getId(), user,
                GlucoseUnit.MILLIGRAMS_PER_DECILITER, CarbsUnit.BREAD_UNITS_10, 2, 12f, 8f, 4f, 2f, false, null);
        when(patientProfileRepository.save(expected)).thenReturn(expected);
        when(patientProfileRepository.findByUserId(user.getId())).thenReturn(existing);

        PatientProfile actual = patientProfileService.updateProfileForPatient(expected, user.getId());

        verify(patientProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(user.getId(), actual.getUserId());
    }

//    private boolean insulinProfilesEqual(InsulinProfile expected, InsulinProfile actual) {
//        if (expected == actual) return true;
//
//        return false;
//    }
}
