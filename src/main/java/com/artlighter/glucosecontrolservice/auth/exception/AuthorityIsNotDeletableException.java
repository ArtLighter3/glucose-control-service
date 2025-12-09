package com.artlighter.glucosecontrolservice.auth.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

/**
 * Исключение выбрасывается тогда, когда была попытка забрать разрешение у определенной роли в том случае, если
 * это разрешение должно быть у роли ВСЕГДА и не может быть удалено.
 */
public class AuthorityIsNotDeletableException extends RuntimeException {
    private Authority authority;
    private Role role;

    public AuthorityIsNotDeletableException(Authority authority, Role role, String message) {
        super(message);
        this.authority = authority;
        this.role = role;
    }

    public AuthorityIsNotDeletableException(Authority authority, Role role) {
        this(authority, role, String.format("Authority \'%s\' is not deletable for the role \'%s\'",
                authority.toString(), role.toString()));
    }

    public Authority getAuthority() {
        return authority;
    }

    public Role getRole() {
        return role;
    }
}
