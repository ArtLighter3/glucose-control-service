package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

public class RoleDoesNotHaveSuchAuthorityException extends AuthoritiesException {

    public RoleDoesNotHaveSuchAuthorityException(Role role, Authority authority, String message) {
        super(role, authority, message);
    }

    public RoleDoesNotHaveSuchAuthorityException(Role role, Authority authority) {
      this(role, authority, String.format("Role '%s' already doesn't have authority '%s'",
              role.name(), authority.name()));
    }

}
