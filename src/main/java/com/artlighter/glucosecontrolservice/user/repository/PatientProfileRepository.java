package com.artlighter.glucosecontrolservice.user.repository;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Integer> {
    PatientProfile getPatientProfileByUserId(int userId);
    Integer getPatientProfileIdByUserId(int userId);
    boolean existsByUserId(int userId);
    void deleteByUserId(int userId);
}
