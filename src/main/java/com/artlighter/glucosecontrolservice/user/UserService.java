package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.service.DoctorProfileService;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.user.repository.UserRepository;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotDoctorException;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Сервис для сбора, добавления, удаления... пользователей системы
 */
@Service
@Transactional
public class UserService {
    private PatientProfileService patientProfileService;
    private DoctorProfileService doctorProfileService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PatientProfileService patientProfileService,
                       DoctorProfileService doctorProfileService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientProfileService = patientProfileService;
        this.doctorProfileService = doctorProfileService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Добавляет пользователя в систему с определенной ролью. При этом если в user уже есть какие-то роли, они
     * будут заменены единичной переданной role!
     * @param user добавляемый пользователь;
     * @param role роль в системе;
     * @return сохраненный пользователь;
     * @throws IllegalArgumentException если user является null;
     * @throws com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException если пользователь
     * с таким именем уже существует;
     */
    public User addUser(User user, Role role) {
        if (user == null)
            throw new IllegalArgumentException("user cannot be null");
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ResourceAlreadyExistsException(user, "user with this username already exists");

        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (role == Role.ROLE_PATIENT) {
            // если по какой-то ошибке не создастся профиль пациента при создании пользователя (отчего может полететь
            // много функционала), то нахождение в одной транзакции не создаст и пользователя, так что все должно быть
            // норм
            try {
                patientProfileService.createDefaultProfileForPatient(user);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        return userRepository.save(user);
    }
    /**
     * Добавляет пользователя в систему.
     * @param user добавляемый пользователь;
     * @return сохраненный пользователь;
     * @throws IllegalArgumentException если user является null или не содержит ролей;
     * @throws com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException если пользователь
     * с таким именем уже существует;
     */
    public User addUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user cannot be null");
        if (user.getRoles() == null || user.getRoles().isEmpty())
            throw new IllegalArgumentException("user roles cannot be null or empty");
        if (userRepository.existsByUsername(user.getUsername()))
            throw new ResourceAlreadyExistsException(user, "user with this username already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        if (user.getRoles().contains(Role.ROLE_PATIENT)) {
            try {
                patientProfileService.createDefaultProfileForPatient(savedUser);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        if (user.getRoles().contains(Role.ROLE_DOCTOR)) {
            try {
                doctorProfileService.createDefaultProfileForDoctor(savedUser);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        return savedUser;
    }

    /**
     * Находит пользователя в системе по его ID.
     * @param id идентификатор пользователя в системе;
     * @return объект пользователя, если был найден; null в случае невозможности достать пользователя
     */
    @Transactional(readOnly = true)
    public User findUserById(int id) {
        User user = userRepository.findByIdWithRoles(id);
        return user;
    }

    /**
     * Находит пользователя в системе по его имени.
     * @param username строковое уникальное имя пользователя
     * @return объект пользователя, если был найден; null в случае невозможности достать пользователя
     */
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsernameWithRoles(username);
        return user;
    }

    /**
     * Получает пользователя в системе по его ID. В отличие от findUserById, не возвращает null в случае ненахождения, а
     * выбрасывает исключение.
     * @param id идентификатор пользователя в системе;
     * @return объект пользователя;
     * @throws com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException если пользователь с
     *  этим id не был найден;
     */
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) throw new ResourceNotFoundException(User.class, "user with ID '" + id + "' not found");

        return user;
    }

    /**
     * Определяет, есть ли у пользователя роль. При отсутствии пользователя возвращает false.
     * @param id идентификатор пользователя в системе
     * @param role роль
     * @return true, если роль есть; false, если роль или сам пользователь не были найдены;
     */
    @Transactional(readOnly = true)
    public boolean hasRole(int id, Role role) {
        User userToCheck = userRepository.findByIdWithRoles(id);

        if (userToCheck.getRoles().contains(Role.ROLE_SUPERUSER)) return true;

        return userToCheck.getRoles().contains(role);
    }

    /**
     * Находит всех пользователей (постранично), в ФИО которых содержится поисковый запрос searchQuery.
     * @param searchQuery поисковая фраза;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с пользователями; пустая коллекция, если ничего не было найдено;
     */
    @Transactional(readOnly = true)
    public Page<User> searchByFullName(String searchQuery, Pageable pageable) {
        return searchByFullNameAndRole(searchQuery, null, pageable);
    }

    /**
     * Находит всех пользователей (постранично) с ролью role, в ФИО которых содержится поисковый запрос searchQuery.
     * @param searchQuery поисковая фраза;
     * @param role роль для фильтрации; если null, то производится поиск по всем ролям;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с пользователями; пустая коллекция, если ничего не было найдено;
     */
    @Transactional(readOnly = true)
    public Page<User> searchByFullNameAndRole(String searchQuery, Role role, Pageable pageable) {
        if (pageable == null) pageable = PageRequest
                .of(0, 10, Sort.by("lastName", "firstName", "middleName"));
        if (searchQuery == null) searchQuery = "";

        Page<Integer> userIDs = role != null ?
                userRepository.searchUserIDsByFullNameAndRolesContaining(searchQuery, role, pageable) :
                userRepository.searchUserIDsByFullName(searchQuery, pageable);
        if (userIDs == null || userIDs.isEmpty()) return Page.empty();

        List<User> users = userRepository.findAllByIdsWithRoles(userIDs.getContent(), userIDs.getSort());

        return new PageImpl<>(users, pageable, userIDs.getTotalElements());
    }

    /**
     * Обновляет профиль больного. Поля с ID, ролями, паролем и логином не учитываются при обновлении этим методом.
     * @param userWithNewInfo информация о пользователе, не учитывая id, роли, пароль и логин;
     * @param userId ID пользователя-больного;
     * @return обновленный профиль больного;
     * @throws ResourceNotFoundException если пользователь с этим ID не существует;
     * @throws IllegalArgumentException если userWithNewInfo равен null;
     */
    public User updateUserInfo(User userWithNewInfo, int userId) {
        if (userWithNewInfo == null) throw new IllegalArgumentException("userWithNewInfo cannot be null");

        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null)
            throw new ResourceNotFoundException(User.class, "user with ID '" + userId + "' not found");

        existingUser.setEmail(userWithNewInfo.getEmail());
        existingUser.setFirstName(userWithNewInfo.getFirstName());
        existingUser.setMiddleName(userWithNewInfo.getMiddleName());
        existingUser.setLastName(userWithNewInfo.getLastName());
        existingUser.setBirthDate(userWithNewInfo.getBirthDate());

        return userRepository.save(existingUser);
    }

    /**
     * Удаляет пользователя из системы. Проигнорирует, если пользователя уже не существует.
     * @param userId ID пользователя, которого необходимо удалить;
     */
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Прикрепляет больного к врачу с проверкой ролей.
     * @param doctorId ID врача;
     * @param patientId ID больного;
     * @return обновленный профиль врача, к которому было произведено прикрепление;
     * @throws ResourceNotFoundException если врач или пациент с переданными ID не были найдены;
     * @throws ResourceAlreadyExistsException если больной уже был прикреплен к врачу;
     * @throws com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotDoctorException если пользователь, к
     * которому прикрепляется больной, не является врачом в системе.
     * @throws com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException если прикрепляемый
     * пользователь не является пользователем-больным в системе.
     */
    public DoctorProfile attachPatientToDoctor(int doctorId, int patientId) {
        if (!hasRole(doctorId, Role.ROLE_DOCTOR)) throw new UserIsNotDoctorException(doctorId);
        if (!hasRole(patientId, Role.ROLE_PATIENT)) throw new UserIsNotPatientException(patientId);

        return doctorProfileService.attachPatientToDoctor(doctorId, patientId);
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
        return doctorProfileService.detachPatientFromDoctor(doctorId, patientId);
    }

    /**
     * Проверяет, прикреплен ли больной к врачу по их ID.
     * @param doctorId ID врача;
     * @param patientId ID больного;
     * @return true, если больной прикреплен;
     *         false, если не прикреплен, либо врач или больной с такими ID не были найдены;
     */
    @Transactional(readOnly = true)
    public boolean isPatientAttached(int doctorId, int patientId) {
        return doctorProfileService.isPatientAttached(doctorId, patientId);
    }
}
