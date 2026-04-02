package com.artlighter.glucosecontrolservice.templates.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.repository.PatientTemplateEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Общий сервис для ресурсов-заготовок (например, блюд, препаратов) для быстрого заполнения записей дневника, чтобы
 * не писать одинаковые методы получения, удаления, обновления.
 * @param <T> класс заготовки, наследник PatientTemplateEntity;
 */
@Transactional
public abstract class TemplateService<T extends PatientTemplateEntity> {
    protected PatientTemplateEntityRepository<T> patientTemplateEntityRepository;

    public TemplateService(PatientTemplateEntityRepository<T> patientTemplateEntityRepository) {
        this.patientTemplateEntityRepository = patientTemplateEntityRepository;
    }

    /**
     * Находит все заготовки этого типа по ID профиля больного.
     * @param patientProfileId ID профиля больного;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с заготовками больных; пустая страница, если не было найдено ничего по данному ID;
     */
    @Transactional(readOnly = true)
    public Page<T> getAllByPatientProfileId(int patientProfileId, Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 10, Sort.by("id.name"));

        Page<T> templates = patientTemplateEntityRepository.getAllByIdPatientProfileId(patientProfileId, pageable);
        if (templates == null) return Page.empty();

        return templates;
    }

    /**
     * Добавляет заготовку этого типа для больного.
     * @param t объект заготовки, наследник PatientTemplateEntity;
     * @param patientProfileId ID профиля больного;
     * @param name наименование заготовки;
     * @return добавленную заготовку;
     * @throws ResourceAlreadyExistsException если заготовка этого типа с таким именем уже существует
     * для этого пользователя
     */
    public T addToPatient(T t, int patientProfileId, String name) {
        //if (t == null)
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);
        if (patientTemplateEntityRepository.existsById(id))
            throw new ResourceAlreadyExistsException(t, "template with name " + name +
                    " for patient with profile id " + patientProfileId + " already exists");

        t.setId(id);

        return patientTemplateEntityRepository.save(t);
    }

    /**
     * Обновляет существующую заготовку этого типа для больного.
     * @param t объект заготовки, наследник PatientTemplateEntity;
     * @param patientProfileId ID профиля больного;
     * @param name наименование заготовки;
     * @return обновленную заготовку;
     * @throws ResourceNotFoundException если заготовка этого типа с таким именем не была найдена
     * для этого пользователя
     */
    public T update(T t, int patientProfileId, String name) {
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);

        if (!patientTemplateEntityRepository.existsById(id))
            throw new ResourceNotFoundException(PatientTemplateEntity.class, "template with name '" + name +
                    "' for patient with profile id '" + patientProfileId + "' not found");

        t.setId(id);

        return patientTemplateEntityRepository.save(t);
    }

    /**
     * Удаляет существующую заготовку этого типа для больного (по ее наименованию). Проигнорирует в случае, если
     * заготовки не было найдено.
     * @param patientProfileId ID профиля больного;
     * @param name наименование заготовки;
     */
    public void deleteFromPatient(int patientProfileId, String name) {
        PatientTemplateEntity.PatientTemplateEntityID id =
                new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);

        patientTemplateEntityRepository.deleteById(id);
    }

//    @Transactional(readOnly = true)
//    public T getByName(int patientProfileId, String name) {
//        PatientTemplateEntity.PatientTemplateEntityID id
//                = new PatientTemplateEntity.PatientTemplateEntityID(patientProfileId, name);
//
//        T t = patientTemplateEntityRepository.findById(id).orElse(null);
//        if (t == null)
//            throw new ResourceNotFoundException("template with name " + name +
//                " for patient with profile id " + patientProfileId + " does not exist");
//
//        return t;
//    }

    /**
     * Находит все заготовки этого типа, в наименовании которых содержится поисковая фраза searchQuery.
     * @param patientProfileId ID профиля больного;
     * @param searchQuery поисковая фраза для поиска по наименованию;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с заготовками больных, соответствующих критерию поиска;
     * пустая страница, если не было найдено ничего по данному ID;
     */
    @Transactional(readOnly = true)
    public Page<T> searchByNameQuery(int patientProfileId, String searchQuery, Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 10, Sort.by("id.name"));

        Page<T> templates = patientTemplateEntityRepository
                .getAllByIdPatientProfileIdAndIdNameContainingIgnoreCase(patientProfileId, searchQuery, pageable);
        if (templates == null) return Page.empty();

        return templates;
    }

}
