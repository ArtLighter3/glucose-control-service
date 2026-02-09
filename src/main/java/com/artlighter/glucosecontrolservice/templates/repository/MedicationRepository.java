package com.artlighter.glucosecontrolservice.templates.repository;

import com.artlighter.glucosecontrolservice.templates.entity.Medication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends PatientTemplateEntityRepository<Medication> {
}
