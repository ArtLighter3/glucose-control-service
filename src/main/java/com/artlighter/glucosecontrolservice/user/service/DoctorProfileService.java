package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.auth.util.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.diary.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.repository.DoctorProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DoctorProfileService {
    private DoctorProfileRepository doctorProfileRepository;
    private PatientProfileService patientProfileService;

    public DoctorProfileService(DoctorProfileRepository doctorProfileRepository,
                                PatientProfileService patientProfileService) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.patientProfileService = patientProfileService;
    }

    @Transactional(readOnly = true)
    public boolean isPatientAttached(int doctorId, int patientId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(doctorId);
        if (doctorProfile == null) return false;

        return doctorProfile.getAttachedPatients().containsKey(patientId);
    }

    public DoctorProfile attachPatientToDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);
        PatientProfile patientProfile = getPatientProfileOrThrowException(patientId);

        doctorProfile.getAttachedPatients().put(patientId, patientProfile);

        return doctorProfileRepository.save(doctorProfile);
    }

    public DoctorProfile detachPatientFromDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);

        doctorProfile.getAttachedPatients().remove(patientId);

        return doctorProfileRepository.save(doctorProfile);
    }

    private PatientProfile getPatientProfileOrThrowException(int patientId) {
        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);
        if (patientProfile == null) throw new ResourceNotFoundException("Patient profile for this user ID not found");
        return patientProfile;
    }

    private DoctorProfile getDoctorProfileOrThrowException(int doctorId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(doctorId);
        if (doctorProfile == null) throw new ResourceNotFoundException("Doctor profile for this user ID not found");
        return doctorProfile;
    }
}
