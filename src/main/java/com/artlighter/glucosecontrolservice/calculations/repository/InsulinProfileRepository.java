package com.artlighter.glucosecontrolservice.calculations.repository;

import com.artlighter.glucosecontrolservice.calculations.entity.InsulinProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsulinProfileRepository extends JpaRepository<InsulinProfile, Integer> {
    InsulinProfile findByPatientProfileId(int profileId);
    boolean existsByPatientProfileId(int profileId);
    void deleteByPatientProfileId(int profileId);
}
