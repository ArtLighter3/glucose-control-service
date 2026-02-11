package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.repository.DoctorProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class DoctorProfileService {
    private DoctorProfileRepository doctorProfileRepository;
    private PatientProfileService patientProfileService;

    @Value("${glucose-control-service.max-page-size}")
    private int maxPageSize = 20;

    public DoctorProfileService(DoctorProfileRepository doctorProfileRepository,
                                PatientProfileService patientProfileService) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.patientProfileService = patientProfileService;
    }

//    @Transactional(readOnly = true)
//    public Page<PatientProfile> getAttachedPatients(int doctorId, Pageable pageable) {
//        if (pageable == null) pageable = PageRequest.of(0, 10);
//        if (pageable.getPageSize() > maxPageSize)
//            pageable = PageRequest.of(pageable.getPageNumber(), maxPageSize, pageable.getSort());
//
//        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);
//
//        return doctorProfileRepository.findAttachedPatients(doctorProfile.getId(), pageable);
//    }

    @Transactional(readOnly = true)
    public boolean isPatientAttached(int doctorId, int patientId) {
//        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(doctorId);
//        if (doctorProfile == null) return false;

        return doctorProfileRepository.existsAttachedPatientsByUserIdAndAttachedPatientsUserId(doctorId, patientId);
        //return doctorProfile.getAttachedPatients().containsKey(patientId);
    }

    public DoctorProfile attachPatientToDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);
        PatientProfile patientProfile = getPatientProfileOrThrowException(patientId);

        Set<PatientProfile> attachedPatients = doctorProfile.getAttachedPatients();
        attachedPatients.add(patientProfile);

        return doctorProfileRepository.save(doctorProfile);
    }

    public DoctorProfile detachPatientFromDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);
        PatientProfile patientProfile = getPatientProfileOrThrowException(patientId);

        Set<PatientProfile> attachedPatients = doctorProfile.getAttachedPatients();
        attachedPatients.remove(patientProfile);

        return doctorProfileRepository.save(doctorProfile);
    }

    private PatientProfile getPatientProfileOrThrowException(int patientId) {
        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");
        return patientProfile;
    }

    private DoctorProfile getDoctorProfileOrThrowException(int doctorId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(doctorId);
        if (doctorProfile == null) throw new ResourceNotFoundException("doctor not found");
        return doctorProfile;
    }
}
