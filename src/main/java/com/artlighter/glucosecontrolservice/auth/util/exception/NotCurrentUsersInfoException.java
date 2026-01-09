package com.artlighter.glucosecontrolservice.auth.util.exception;

/**
 * Исключение выбрасывается, когда пользователь пытается прочитать не свои данные, то есть получить доступ
 * к чужим записям дневника, настройкам и т.д. (в случае, если у него нет на это права).
 */
public class NotCurrentUsersInfoException extends RuntimeException {
    public NotCurrentUsersInfoException(String message) {
        super(message);
    }
}
