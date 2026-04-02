package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Integer> {

    boolean existsById(int userId);
//    @Query("SELECT count(*) FROM DoctorProfile.attachedPatients ap WHERE ap.id = :patientProfileId")
//    int countAttachedPatientsByPatientProfileId(int doctorProfileId, int patientProfileId);
    boolean existsAttachedPatientsByIdAndAttachedPatientsUserId(int doctorUserId, int patientUserId);

}
