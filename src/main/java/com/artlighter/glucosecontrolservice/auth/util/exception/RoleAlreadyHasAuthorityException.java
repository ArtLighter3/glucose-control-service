package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

public class RoleAlreadyHasAuthorityException extends AuthoritiesException {
    public RoleAlreadyHasAuthorityException(Role role, Authority authority, String message) {
        super(role, authority, message);
    }

    public RoleAlreadyHasAuthorityException(Role role, Authority authority) {
        this(role, authority, String.format("Role '%s' already has authority '%s'",
                role.name(), authority.name()));
    }
}
