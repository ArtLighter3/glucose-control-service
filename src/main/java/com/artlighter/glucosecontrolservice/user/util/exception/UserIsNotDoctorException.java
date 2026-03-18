package com.artlighter.glucosecontrolservice.user.util.exception;

import com.artlighter.glucosecontrolservice.user.entity.User;

/**
 * Исключение, выбрасываемое в том случае, когда с пользователем без роли врача пытаются совершить действия,
 * которые можно совершить только с врачами.
 */
public class UserIsNotDoctorException extends RuntimeException {
    private int userId;

    public UserIsNotDoctorException(int userId) {
        super("user is not doctor");
        this.userId = userId;
    }

    public UserIsNotDoctorException(int userId, String message) {
        super(message);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
