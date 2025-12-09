package com.artlighter.glucosecontrolservice.auth.repository;

import com.artlighter.glucosecontrolservice.auth.AuthorityService;
import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

import java.util.Map;

public interface AuthorityRepository {
    Authority addAuthority(Role role, Authority authority, boolean isDeletable);
    Authority removeAuthority(Role role, Authority authority);
    Map<Authority, Boolean> getRoleAuthorities(Role role);
}
