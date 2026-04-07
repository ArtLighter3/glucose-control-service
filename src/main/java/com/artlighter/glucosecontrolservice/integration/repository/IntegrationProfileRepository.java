package com.artlighter.glucosecontrolservice.integration.repository;

import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationProfileRepository extends JpaRepository<IntegrationProfile, Integer> {
    IntegrationProfile findByPatientProfileId(int patientProfileId);
    boolean existsByPatientProfileId(int patientProfileId);
}
