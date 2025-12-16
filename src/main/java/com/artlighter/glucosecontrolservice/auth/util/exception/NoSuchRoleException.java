package com.artlighter.glucosecontrolservice.auth.util.exception;

/**
 * Исключение, выбрасываемое в случае попытки
 * добавления, удаления или модификации роли, которой нет в списке ролей системы
 */
public class NoSuchRoleException extends NoSuchEnumerableConstantException {

    /**
     * Конструктор
     * @param nonExistentRoleName строковое имя переданной роли, которой нет в системе
     * @param message сообщение исключения
     */
    public NoSuchRoleException(String nonExistentRoleName, String message) {
        super(nonExistentRoleName, "Role", message);
    }

    /**
     * Конструктор, автоматически инициализирующий сообщение исключения
     * @param nonExistentRoleName строковое имя переданной роли, которой нет в системе
     */
    public NoSuchRoleException(String nonExistentRoleName) {
        super(nonExistentRoleName, "Role");
    }

}
