package com.artlighter.glucosecontrolservice.templates.repository;

import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends PatientTemplateEntityRepository<Medication> {
    @Query("SELECT m FROM Medication m WHERE m.id.patientProfileId = :profileId " +
            "AND LOWER(m.id.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Medication> getAllByIdPatientProfileIdAndIdNameContainingIgnoreCase(@Param("profileId") int patientProfileId,
                                                                             @Param("query") String searchQuery,
                                                                             Pageable pageable);
}
