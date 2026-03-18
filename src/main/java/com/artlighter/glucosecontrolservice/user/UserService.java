package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Сервис для сбора, добавления, удаления... пользователей системы
 */
@Service
public class UserService {
    private PatientProfileService patientProfileService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PatientProfileService patientProfileService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientProfileService = patientProfileService;
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
            //patientProfileService.createDefaultProfileForPatient(user.getId());
            try {
                patientProfileService.createDefaultProfileForPatient(user);
            } catch (ResourceAlreadyExistsException ignored) {}
        }

        return userRepository.save(user);
    }

    /**
     * Находит пользователя в системе по его ID.
     * @param id идентификатор пользователя в системе;
     * @return объект пользователя, если был найден; null в случае невозможности достать пользователя
     */
    public User findUserById(int id) {
        User user = userRepository.findByIdWithRoles(id);
        return user;
    }

    /**
     * Находит пользователя в системе по его имени.
     * @param username строковое уникальное имя пользователя
     * @return объект пользователя, если был найден; null в случае невозможности достать пользователя
     */
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsernameWithRoles(username);
        return user;
    }

    /**
     * Определяет, есть ли у пользователя роль без необходимости загрузки всего пользователя
     * @param id идентификатор пользователя в системе
     * @param role роль
     * @return true, если роль есть; false иначе;
     */
    public boolean hasRole(int id, Role role) {
        return userRepository.existsByIdAndRolesContaining(id, role);
    }
}
