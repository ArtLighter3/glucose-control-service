package com.artlighter.glucosecontrolservice.user.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.UserService;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.repository.DoctorProfileRepository;
import com.artlighter.glucosecontrolservice.user.repository.PatientProfileRepository;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotDoctorException;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Сервис для доступа и модификации профилей врачей, в том числе и списка прикрепленных к ним больных.
 */
@Service
@Transactional
public class DoctorProfileService {
    private DoctorProfileRepository doctorProfileRepository;
    private PatientProfileService patientProfileService;

    public DoctorProfileService(DoctorProfileRepository doctorProfileRepository,
                                PatientProfileService patientProfileService) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.patientProfileService = patientProfileService;
    }

    /**
     * Создает стандартный профиль врача для пользователя.
     * @param user пользователь, для которого нужно сохранить профиль врача;
     * @return сохраненный DoctorProfile;
     * @throws ResourceAlreadyExistsException если профиль врача уже существует для этого пользователя.
     * @throws IllegalArgumentException если user равен null;
     * @throws UserIsNotDoctorException если пользователь не является врачом;
     */
    public DoctorProfile createDefaultProfileForDoctor(User user) {
        if (user == null) throw new IllegalArgumentException("user cannot be null");

        if (doctorProfileRepository.existsById(user.getId()))
            throw new ResourceAlreadyExistsException(new DoctorProfile(user.getId()),
                    "doctor profile for this user already exists");
        if (!user.getRoles().contains(Role.ROLE_DOCTOR))
            throw new UserIsNotDoctorException(user.getId());

        return doctorProfileRepository.save(new DoctorProfile(user.getId()));
    }

//    @Transactional(readOnly = true)
//    public Page<PatientProfile> getAttachedPatients(int doctorId, Pageable pageable) {
//        if (pageable == null) pageable = PageRequest.of(0, 10);
//        if (pageable.getPageSize() > maxPageSize)
//            pageable = PageRequest.of(pageable.getPageNumber(), maxPageSize, pageable.getSort());
//
//        DoctorProfile doctorProfile = getDoctorProfileOrThrowException(doctorId);
//
//        return doctorProfileRepository.findAttachedPatients(doctorProfile.getId(), pageable);
//    }

    /**
     * Проверяет, прикреплен ли больной к врачу по их ID.
     * @param doctorId ID врача;
     * @param patientId ID больного;
     * @return true, если больной прикреплен;
     *         false, если не прикреплен, либо врач или больной с такими ID не были найдены;
     */
    @Transactional(readOnly = true)
    public boolean isPatientAttached(int doctorId, int patientId) {
        return doctorProfileRepository.existsAttachedPatientsByIdAndAttachedPatientsUserId(doctorId, patientId);
    }

    /**
     * Прикрепляет больного к врачу. Не проверяет наличие права врача или пациента!
     * @param doctorId ID врача;
     * @param patientId ID больного;
     * @return обновленный профиль врача, к которому было произведено прикрепление;
     * @throws ResourceNotFoundException если врач или пациент с переданными ID не были найдены;
     * @throws ResourceAlreadyExistsException если больной уже был прикреплен к врачу;
     */
    public DoctorProfile attachPatientToDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getByUserId(doctorId);
        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);

        Set<PatientProfile> attachedPatients = doctorProfile.getAttachedPatients();
        boolean attached = attachedPatients.add(patientProfile);
        if (!attached)
            throw new ResourceAlreadyExistsException(patientProfile, "patient is already attached to this doctor");

        return doctorProfileRepository.save(doctorProfile);
    }

    /**
     * Открепляет больного от врача.
     * @param doctorId ID врача;
     * @param patientId ID больного;
     * @return обновленный профиль врача, у которого был удален прикрепленный больной;
     * @throws ResourceNotFoundException если врач или пациент с переданными ID не были найдены, либо если
     *                                   больной уже откреплен;
     */
    public DoctorProfile detachPatientFromDoctor(int doctorId, int patientId) {
        DoctorProfile doctorProfile = getByUserId(doctorId);
        PatientProfile patientProfile = patientProfileService.getByUserId(patientId);

        Set<PatientProfile> attachedPatients = doctorProfile.getAttachedPatients();
        boolean detached = attachedPatients.remove(patientProfile);
        if (!detached)
            throw new ResourceNotFoundException(PatientProfile.class, "patient '"
                    + patientId + "' is not attached to doctor '" + doctorId + "'");

        return doctorProfileRepository.save(doctorProfile);
    }

    /**
     * Находит прикрепленных к врачу больных (их профилей) по ID врача.
     * @param doctorId идентификатор врача в системе;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с профилями больных, прикрепленных к врачу;
     * @throws ResourceNotFoundException если врач не был найден в системе;
     */
    public Page<PatientProfile> getAttachedPatients(int doctorId, Pageable pageable) {
        DoctorProfile doctorProfile = getByUserId(doctorId);

        return patientProfileService.getPatientsAttachedToDoctor(doctorProfile.getId(), pageable);
    }

    /**
     * Находит список прикрепленных к врачу больных (их профилей) по ID врача, чьи ФИО содержат в себе
     * поисковую фразу searchQuery.
     * @param doctorId идентификатор врача в системе;
     * @param searchQuery поисковая фраза для поиска по ФИО;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с профилями больных, прикрепленных к врачу, соответствующих критерию поиска;
     * @throws ResourceNotFoundException если врач не был найден в системе;
     */
    public Page<PatientProfile> searchAttachedPatients(int doctorId, String searchQuery, Pageable pageable) {
        DoctorProfile doctorProfile = getByUserId(doctorId);

        return patientProfileService.getPatientsAttachedToDoctor(doctorProfile.getId(), searchQuery, pageable);
    }

    /**
     * Находит профиль врача по ID этого врача.
     * @param doctorId ID пользователя-врача;
     * @return DoctorProfile, соответствующий ID врача; никогда не null;
     * @throws ResourceNotFoundException если не было найдено профиля врача для этого doctorId;
     */
    public DoctorProfile getByUserId(int doctorId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(doctorId).orElse(null);
        if (doctorProfile == null)
            throw new ResourceNotFoundException(DoctorProfile.class, "doctor with ID '" + doctorId  + "' not found");

        return doctorProfile;
    }


//    /**
//     * Сохраняет профиль врача.
//     * @param doctorProfile обновляемый профиль врача;
//     * @param userId ID пользователя-врача;
//     * @return обновленный профиль врача;
//     * @throws ResourceNotFoundException если профиль врача не существует;
//     * @throws IllegalArgumentException если doctorProfile равен null;
//     */
//    public DoctorProfile up(DoctorProfile doctorProfile) {
//        if (doctorProfile == null) throw new IllegalArgumentException("doctorProfile cannot be null");
//
//        DoctorProfile existingProfile = getByUserId(userId);
//
//        //patientProfile.setUserId(userId);
//        doctorProfile.setId(userId);
//        return doctorProfileRepository.save(doctorProfile);
//    }
}
