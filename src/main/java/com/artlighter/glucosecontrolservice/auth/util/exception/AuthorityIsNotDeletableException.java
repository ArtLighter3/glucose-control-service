package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

/**
 * Исключение выбрасывается тогда, когда была попытка забрать разрешение у определенной роли в том случае, если
 * это разрешение должно быть у роли ВСЕГДА и не может быть удалено.
 */
public class AuthorityIsNotDeletableException extends AuthoritiesException {

    public AuthorityIsNotDeletableException(Role role, Authority authority, String message) {
        super(role, authority, message);
    }

    public AuthorityIsNotDeletableException(Role role, Authority authority) {
        this(role, authority, String.format("Authority \'%s\' is not deletable for the role \'%s\'",
                authority.name(), role.name()));
    }

}
