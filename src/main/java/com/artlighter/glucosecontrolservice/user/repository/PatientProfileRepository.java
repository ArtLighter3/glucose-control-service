package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Integer> {
    PatientProfile findByUserId(int userId);
    PatientProfile findByUserUsername(String username);
    //Integer getPatientProfileIdByUserId(int userId);
    boolean existsByUserId(int userId);
    void deleteByUserId(int userId);

    @Query("SELECT pp FROM PatientProfile pp JOIN FETCH pp.user u " +
            "JOIN pp.doctors d WHERE d.id = :doctorProfileId")
    Page<PatientProfile> getPatientsAttachedToDoctorByDoctorId(@Param("doctorProfileId") int doctorProfileId,
                                                                  Pageable pageable);

    @Query("SELECT pp FROM PatientProfile pp JOIN FETCH pp.user u " +
            "JOIN pp.doctors d WHERE u.username LIKE :searchQuery AND d.id = :doctorProfileId")
    Page<PatientProfile> searchPatientsAttachedToDoctorByDoctorId(@Param("doctorProfileId") int doctorProfileId,
                                                               @Param("searchQuery") String searchQuery,
                                                               Pageable pageable);
}

