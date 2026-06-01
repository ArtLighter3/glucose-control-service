package com.artlighter.glucosecontrolservice.templates.repository;

import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends PatientTemplateEntityRepository<Meal> {
    @Query("SELECT m FROM Meal m WHERE m.id.patientProfileId = :profileId " +
            "AND LOWER(m.id.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Meal> getAllByIdPatientProfileIdAndIdNameContainingIgnoreCase(@Param("profileId") int patientProfileId,
                                                                             @Param("query") String searchQuery,
                                                                             Pageable pageable);
}
