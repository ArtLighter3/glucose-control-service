package com.artlighter.glucosecontrolservice.user.util.exception;

import com.artlighter.glucosecontrolservice.user.entity.User;

/**
 * Исключение, выбрасываемое в том случае, когда с пользователем без роли больного пытаются совершить действия,
 * которые можно совершить только с больными.
 */
public class UserIsNotPatientException extends RuntimeException {
    private int userId;

    public UserIsNotPatientException(int userId) {
        super("user is not patient");
        this.userId = userId;
    }

    public UserIsNotPatientException(int userId, String message) {
        super(message);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
