package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.DoctorProfile;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.repository.DoctorProfileRepository;
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

        if (user.getRoles().contains(Role.ROLE_PATIENT)) {
            try {
                patientProfileService.createDefaultProfileForPatient(user);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        if (user.getRoles().contains(Role.ROLE_DOCTOR)) {
            try {
                doctorProfileService.createDefaultProfileForDoctor(user);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        return userRepository.save(user);
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
     * Определяет, есть ли у пользователя роль без необходимости загрузки всего пользователя
     * @param id идентификатор пользователя в системе
     * @param role роль
     * @return true, если роль есть; false иначе;
     */
    @Transactional(readOnly = true)
    public boolean hasRole(int id, Role role) {
        return userRepository.existsByIdAndRolesContaining(id, role);
    }

    /**
     * Находит всех пользователей постранично, в фамилии которых содержится поисковый запрос searchQuery.
     * @param searchQuery поисковая фраза;
     * @param pageable объект с информацией о пагинации;
     * @return текущая страница с пользователями; пустая коллекция, если ничего не было найдено;
     */
    @Transactional(readOnly = true)
    public Slice<User> searchByLastName(String searchQuery, Pageable pageable) {
        if (pageable == null) pageable = PageRequest.of(0, 10, Sort.by("lastName"));

        Slice<User> users = userRepository.searchAllByLastNameContainingIgnoreCase(searchQuery, pageable);
        if (users == null) return Page.empty();

        return users;
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
}
