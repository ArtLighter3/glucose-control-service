package com.artlighter.glucosecontrolservice.templates.repository;

import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PatientTemplateEntityRepository<T extends PatientTemplateEntity>
        extends JpaRepository<T, PatientTemplateEntity.PatientTemplateEntityID> {

    Page<T> getAllByIdPatientProfileId(int patientProfileId, Pageable pageable);
    Page<T> getAllByIdPatientProfileIdAndIdNameContainingIgnoreCase(int patientProfileId, String searchQuery,
                                                                    Pageable pageable);
}
