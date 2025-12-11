package com.artlighter.glucosecontrolservice.auth.util.exception;

public class NoSuchRoleException extends NoSuchEnumerableConstantException {

    public NoSuchRoleException(String nonExistentRoleName, String message) {
        super(nonExistentRoleName, "Role", message);
    }

    public NoSuchRoleException(String nonExistentRoleName) {
        this(nonExistentRoleName, "Role");
    }

}
