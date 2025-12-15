package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.auth.entity.Authority;
import com.artlighter.glucosecontrolservice.auth.entity.Role;

/**
 * Исключение, выбрасываемое, если при добавлении права к роли оказывается, что это право уже есть у роли
 */
public class RoleAlreadyHasAuthorityException extends AuthoritiesException {

    /**
     * Конструктор
     * @param role роль, с которой связано исключение
     * @param authority право, с которым связано исключение (то есть уже имеющееся у роли)
     * @param message сообщение исключения
     */
    public RoleAlreadyHasAuthorityException(Role role, Authority authority, String message) {
        super(role, authority, message);
    }

    /**
     * Конструктор, автоматически инициализирующий сообщение исключения
     * @param role роль, с которой связано исключение
     * @param authority право, с которым связано исключение (то есть уже имеющееся у роли)
     */
    public RoleAlreadyHasAuthorityException(Role role, Authority authority) {
        this(role, authority, String.format("Role '%s' already has authority '%s'",
                role.name(), authority.name()));
    }
}
