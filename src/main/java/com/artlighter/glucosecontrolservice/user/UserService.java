package com.artlighter.glucosecontrolservice.user;

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

    public User addUser(User user, Role role) {
        if (user == null) return null;

        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (role == Role.ROLE_PATIENT) {
            patientProfileService.createDefaultProfileForPatient(user.getId());
        }

        return userRepository.save(user);
    }

    /**
     * Находит пользователя в системе по его имени
     * @param username строковое уникальное имя пользователя
     * @return объект пользователя, если был найден; null в случае невозможности достать пользователя
     */
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }
}
