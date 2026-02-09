package com.artlighter.glucosecontrolservice.templates.repository;

import com.artlighter.glucosecontrolservice.templates.entity.Meal;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends PatientTemplateEntityRepository<Meal> {
}
