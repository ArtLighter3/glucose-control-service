package com.artlighter.glucosecontrolservice.auth;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.util.exception.AuthorityIsNotDeletableException;
import com.artlighter.glucosecontrolservice.auth.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorityService {
    private AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority addDeletableAuthority(Role role, Authority authority) {
        return addAuthority(role, authority, true);
    }

    public Authority addUndeletableAuthority(Role role, Authority authority) {
        return addAuthority(role, authority, false);
    }

    public Authority addAuthority(Role role, Authority authority, boolean isDeletable) {
        if (role == null || authority == null) return null;

        return authorityRepository.addAuthority(role, authority, isDeletable);
    }

    public void addDeletableAuthorities(Role role, Authority... authorities) {
        for (Authority authority : authorities) {
            addAuthority(role, authority, true);
        }
    }

    public void addUndeletableAuthorities(Role role, Authority... authorities) {
        for (Authority authority : authorities) {
            addAuthority(role, authority, false);
        }
    }

    public Authority removeAuthority(Role role, Authority authority) {
        if (role == null || authority == null) return null;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        if (authorities == null || !authorities.containsKey(authority)) return null;

        boolean isDeletable = authorities.get(authority);
        if (!isDeletable) throw new AuthorityIsNotDeletableException(role, authority);

        return authorityRepository.removeAuthority(role, authority);
    }

    @Transactional(readOnly = true)
    public boolean hasAuthority(Role role, Authority authority) {
        if (role == null) return false;
        if (role == Role.ROLE_SUPERUSER) return true;
        if (authority == null) return false;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        return authorities != null && authorities.containsKey(authority);
    }

    @Transactional(readOnly = true)
    public Set<Authority> getRoleAuthorities(Role role) {
        if (role == null) return null;

        Map<Authority, Boolean> authorities = authorityRepository.getRoleAuthorities(role);
        if (authorities == null) return Collections.emptySet();
        return authorities.keySet();
    }

    public Set<Role> getAllRoles() {
        return Arrays.stream(Role.values()).collect(Collectors.toSet());
    }

    public Set<Authority> getAllAuthorities() {
        return Arrays.stream(Authority.values()).collect(Collectors.toSet());
    }

}
