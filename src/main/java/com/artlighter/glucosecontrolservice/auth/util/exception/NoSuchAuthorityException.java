package com.artlighter.glucosecontrolservice.auth.util.exception;

public class NoSuchAuthorityException extends NoSuchEnumerableConstantException {

    public NoSuchAuthorityException(String nonExistentAuthorityName, String message) {
        super(nonExistentAuthorityName, "Authority", message);
    }

    public NoSuchAuthorityException(String nonExistentAuthorityName) {
        this(nonExistentAuthorityName, "Authority");
    }

}