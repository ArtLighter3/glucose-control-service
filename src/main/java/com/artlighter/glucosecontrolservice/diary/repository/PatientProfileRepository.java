package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Integer> {
    PatientProfile getPatientProfileByUserId(int userId);
}
