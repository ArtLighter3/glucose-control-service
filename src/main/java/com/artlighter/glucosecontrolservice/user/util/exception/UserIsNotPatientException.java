package com.artlighter.glucosecontrolservice.user.util.exception;

import com.artlighter.glucosecontrolservice.user.entity.User;

/**
 * Исключение, выбрасываемое в том случае, когда с пользователем без роли больного пытаются совершить действия,
 * которые можно совершить только с больными.
 */
public class UserIsNotPatientException extends RuntimeException {
    private User user;

    public UserIsNotPatientException(User user) {
        super("user is not patient");
        this.user = user;
    }

    public UserIsNotPatientException(User user, String message) {
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
