package com.artlighter.glucosecontrolservice.user;

import com.artlighter.glucosecontrolservice.auth.service.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.entity.User;
import com.artlighter.glucosecontrolservice.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для сбора, добавления, удаления... пользователей системы
 */
@Service
public class UserService implements UserDetailsService {
    private AuthorityService authorityService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       AuthorityService authorityService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user, Role role) {
        if (user == null) return null;

        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
            Set<Authority> authorities = authorityService.getRoleAuthorities(role);
            grantedAuthorities.addAll(authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.name()))
                    .collect(Collectors.toSet()));
        }

        //TODO возможно, стоит добавить все роли, если роль - суперпользователь
        user.setGrantedAuthorities(grantedAuthorities);
        return user;
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
