package com.artlighter.glucosecontrolservice.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Map;
import java.util.Objects;

//@Entity
//@Table(name = "")
public class RoleWithAuthorities {
    private Role role;
    private Map<Authority, Boolean> authorities;

    public RoleWithAuthorities(Role role, Map<Authority, Boolean> authorities) {
        this.role = role;
        this.authorities = authorities;
    }

    public Map<Authority, Boolean> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Map<Authority, Boolean> authorities) {
        this.authorities = authorities;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleWithAuthorities that = (RoleWithAuthorities) o;
        return role == that.role && Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
