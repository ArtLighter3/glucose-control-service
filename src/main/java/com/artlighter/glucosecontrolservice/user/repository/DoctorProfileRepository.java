package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Integer> {

    boolean existsById(int userId);
//    @Query("SELECT count(*) FROM DoctorProfile.attachedPatients ap WHERE ap.id = :patientProfileId")
//    int countAttachedPatientsByPatientProfileId(int doctorProfileId, int patientProfileId);
    boolean existsAttachedPatientsByIdAndAttachedPatientsUserId(int doctorUserId, int patientUserId);

    DoctorProfile getByPersonalSecret(String personalSecret);
    boolean existsByPersonalSecret(String personalSecret);

    @Query("SELECT dp FROM DoctorProfile dp JOIN FETCH dp.user u " +
            "JOIN dp.attachedPatients ap WHERE ap.userId = :patientProfileId")
    Page<DoctorProfile> getDoctorsAttachedToPatientByPatientId(@Param("patientProfileId") int patientProfileId,
                                                               Pageable pageable);
}
