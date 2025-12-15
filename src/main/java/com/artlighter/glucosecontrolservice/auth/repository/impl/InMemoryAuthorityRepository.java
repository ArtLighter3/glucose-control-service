package com.artlighter.glucosecontrolservice.auth.repository.impl;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;
import com.artlighter.glucosecontrolservice.auth.repository.AuthorityRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * WIP
 */
public class InMemoryAuthorityRepository implements AuthorityRepository {
    private Map<Role, Map<Authority, Boolean>> roleAuthorities = new HashMap<>();

    private Role addRole(Role role) {
        if (role == null || roleAuthorities == null || roleAuthorities.containsKey(role)) return null;

        roleAuthorities.put(role, new HashMap<>());
        return role;
    }

    @Override
    public Authority addAuthority(Role role, Authority authority, boolean isDeletable) {
        if (roleAuthorities == null || role == null || authority == null) return null;

        Map<Authority, Boolean> authorities = roleAuthorities.get(role);
        if (authorities == null) {
            addRole(role);
            authorities = roleAuthorities.get(role);
        }

        authorities.put(authority, isDeletable);
        return authority;
    }

    @Override
    public Authority removeAuthority(Role role, Authority authority) {
        if (roleAuthorities == null || role == null || authority == null) return null;

        Map<Authority, Boolean> authorities = roleAuthorities.get(role);
        if (authorities == null) return null;

        boolean removed = authorities.remove(role);
        return removed ? authority : null;
    }

    private Role removeRole(Role role) {
        if (roleAuthorities == null || role == null) return null;

        Map<Authority, Boolean> authorities = roleAuthorities.remove(role);
        return authorities != null ? role : null;
    }

    @Override
    public Map<Authority, Boolean> getRoleAuthorities(Role role) {
        if (roleAuthorities == null) return null;
        Map<Authority, Boolean> authorities = roleAuthorities.get(role);
        return authorities;
    }
}
