package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.entity.*;
import com.artlighter.glucosecontrolservice.user.repository.DoctorProfileRepository;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotDoctorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(DoctorProfileService.class)
public class DoctorProfileServiceTests {
    @MockitoBean
    private DoctorProfileRepository doctorProfileRepository;
    @MockitoBean
    private PatientProfileService patientProfileService;
    @Autowired
    private DoctorProfileService doctorProfileService;

    @Test
    public void createDefaultProfileForDoctor_UserIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> doctorProfileService.createDefaultProfileForDoctor(null));
    }

    @Test
    public void createDefaultProfileForDoctor_ProfileAlreadyExists_ThrowsResourceAlreadyExistsException() {
        when(doctorProfileRepository.existsById(1)).thenReturn(true);
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_DOCTOR));

        assertThrows(ResourceAlreadyExistsException.class, () ->
                doctorProfileService.createDefaultProfileForDoctor(user));
    }

    @Test
    public void createDefaultProfileForDoctor_UserIsNotDoctor_ThrowsUserIsNotDoctorException() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_PATIENT));

        assertThrows(UserIsNotDoctorException.class, () ->
                doctorProfileService.createDefaultProfileForDoctor(user));
    }

    @Test
    public void createDefaultProfileForPatient_CallsRepositoryToSaveDoctorProfileAndReturnsSaved() {
        User user = new User();
        user.setId(1);
        user.setRoles(Set.of(Role.ROLE_DOCTOR));
        DoctorProfile expected = new DoctorProfile(user.getId());
        when(doctorProfileRepository.save(expected)).thenReturn(expected);

        DoctorProfile actual = doctorProfileService.createDefaultProfileForDoctor(user);

        verify(doctorProfileRepository).save(expected);
        assertEquals(expected, actual);
        assertEquals(user.getId(), actual.getId());
        assertTrue(expected.getAttachedPatients() == null || expected.getAttachedPatients().isEmpty());
    }

    @Test
    public void isPatientAttached_ReturnsTrueOnlyIfRepositoryFindsAttachedPatient() {
        when(doctorProfileRepository.existsAttachedPatientsByIdAndAttachedPatientsUserId(2, 1))
                .thenReturn(true);
        when(doctorProfileRepository.existsAttachedPatientsByIdAndAttachedPatientsUserId(2, 3))
                .thenReturn(true);
        when(doctorProfileRepository.existsAttachedPatientsByIdAndAttachedPatientsUserId(2, 4))
                .thenReturn(true);

        assertTrue(doctorProfileService.isPatientAttached(2, 1));
        assertTrue(doctorProfileService.isPatientAttached(2, 3));
        assertTrue(doctorProfileService.isPatientAttached(2, 4));

        assertFalse(doctorProfileService.isPatientAttached(2, 5));
        assertFalse(doctorProfileService.isPatientAttached(3, 1));
        assertFalse(doctorProfileService.isPatientAttached(2, 2));
    }

//    @Test
//    public void attachPatientToDoctor_DoctorOrPatientNotFound_ThrowsResourceNotFoundException() {
//        when(patientProfileService.getByUserId(3))
//                .thenThrow(new ResourceNotFoundException(PatientProfile.class, ""));
//        when(patientProfileService.getByUserId(0)).thenReturn(new PatientProfile());
//        when(doctorProfileRepository.findById(2)).thenReturn(Optional.empty());
//        when(doctorProfileRepository.findById(1)).thenReturn(Optional.of(new DoctorProfile()));
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//           doctorProfileService.attachPatientToDoctor(2, 0);
//        });
//        assertThrows(ResourceNotFoundException.class, () -> {
//            doctorProfileService.attachPatientToDoctor(1, 3);
//        });
//        assertThrows(ResourceNotFoundException.class, () -> {
//            doctorProfileService.attachPatientToDoctor(2, 3);
//        });
//    }

//    @Test
//    public void attachPatientToDoctor_AttachedUserIsNotPatient_ThrowsResourceNotFoundException() {
//
//    }
}
