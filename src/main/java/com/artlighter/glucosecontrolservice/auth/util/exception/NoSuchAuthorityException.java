package com.artlighter.glucosecontrolservice.auth.util.exception;

/**
 * Исключение, выбрасываемое в случае попытки добавления или удаления права у роли, которого нет в списке прав системы
 */
public class NoSuchAuthorityException extends NoSuchEnumerableConstantException {

    /**
     * Конструктор
     * @param nonExistentAuthorityName строковое имя переданного права, которого нет в системе
     * @param message сообщение исключения
     */
    public NoSuchAuthorityException(String nonExistentAuthorityName, String message) {
        super(nonExistentAuthorityName, "Authority", message);
    }

    /**
     * Конструктор, автоматически инициализирующий сообщение исключения
     * @param nonExistentAuthorityName строковое имя переданного права, которого нет в системе
     */
    public NoSuchAuthorityException(String nonExistentAuthorityName) {
        super(nonExistentAuthorityName, "Authority");
    }

}