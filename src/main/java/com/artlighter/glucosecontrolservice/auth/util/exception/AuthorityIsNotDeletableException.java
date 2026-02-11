package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.user.entity.Authority;
import com.artlighter.glucosecontrolservice.user.entity.Role;

/**
 * Исключение выбрасывается тогда, когда была попытка отозвать право у определенной роли в том случае, если
 * это право должно быть у роли ВСЕГДА и не может быть отозвано.
 */
public class AuthorityIsNotDeletableException extends AuthoritiesException {

    /**
     * Конструктор
     * @param role роль, с которой связано исключение
     * @param authority право, с которым связано исключение
     * @param message сообщение исключения
     */
    public AuthorityIsNotDeletableException(Role role, Authority authority, String message) {
        super(role, authority, message);
    }

    /**
     * Конструктор, автоматически инициализирующий сообщение исключения
     * @param role роль, с которой связано исключение
     * @param authority право, с которым связано исключение
     */
    public AuthorityIsNotDeletableException(Role role, Authority authority) {
        this(role, authority, String.format("Authority \'%s\' is not deletable for the role \'%s\'",
                authority.name(), role.name()));
    }

}
