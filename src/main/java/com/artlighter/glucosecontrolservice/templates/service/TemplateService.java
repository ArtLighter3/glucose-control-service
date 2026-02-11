package com.artlighter.glucosecontrolservice.templates.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.repository.PatientTemplateEntityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class TemplateService<T extends PatientTemplateEntity> {
    protected PatientTemplateEntityRepository<T> patientTemplateEntityRepository;
    @Value("${glucose-control-service.max-page-size}")
    protected int maxPageSize = 20;

    public TemplateService(PatientTemplateEntityRepository<T> patientTemplateEntityRepository) {
        this.patientTemplateEntityRepository = patientTemplateEntityRepository;
    }

    @Transactional(readOnly = true)
    public Page<T> getAllByPatientProfileId(int patientProfileId, Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 10, Sort.by("id.name"));
        if (pageable.getPageSize() > maxPageSize)
            pageable = PageRequest.of(pageable.getPageNumber(), maxPageSize, pageable.getSort());

        Page<T> templates = patientTemplateEntityRepository.getAllByIdPatientProfileId(patientProfileId, pageable);
        if (templates == null) return Page.empty();

        return templates;
    }

    public T addToPatient(T t, int patientProfileId, String name) {
        //if (t == null)
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);
        if (patientTemplateEntityRepository.existsById(id))
            throw new ResourceAlreadyExistsException(t, "template with this name for this patient already exists");

        t.setId(id);

        return patientTemplateEntityRepository.save(t);
    }

    public T update(T t, int patientProfileId, String name) {
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);

        if (!patientTemplateEntityRepository.existsById(id))
            throw new ResourceNotFoundException("template with this name for this user does not exist");

        t.setId(id);

        return patientTemplateEntityRepository.save(t);
    }

    public void deleteFromPatient(int patientProfileId, String name) {
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);

        patientTemplateEntityRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public T getByName(int patientProfileId, String name) {
        PatientTemplateEntity.PatientTemplateEntityID id
                = new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);

        T t = patientTemplateEntityRepository.findById(id).orElse(null);
        if (t == null) throw new ResourceNotFoundException("template with this name for this user does not exist");

        return t;
    }

    @Transactional(readOnly = true)
    public Page<T> searchByNameQuery(int patientProfileId, String searchQuery, Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 10);
        if (pageable.getPageSize() > 20) pageable = PageRequest.of(pageable.getPageNumber(), 20);

        Page<T> templates = patientTemplateEntityRepository
                .getAllByIdPatientProfileIdAndIdNameContaining(patientProfileId, searchQuery, pageable);
        if (templates == null) return Page.empty();

        return templates;
    }

}
