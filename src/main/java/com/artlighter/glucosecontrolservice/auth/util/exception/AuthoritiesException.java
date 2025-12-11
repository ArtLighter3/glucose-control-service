package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

public class AuthoritiesException extends RuntimeException {
    private Authority authority;
    private Role role;

    public AuthoritiesException(Role role, Authority authority, String message) {
      super(message);
      this.authority = authority;
      this.role = role;
    }

    public Authority getAuthority() {
      return authority;
    }

    public Role getRole() {
      return role;
    }

}
