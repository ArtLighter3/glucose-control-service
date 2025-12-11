package com.artlighter.glucosecontrolservice.auth.util.exception;

import java.util.Enumeration;

public class NoSuchEnumerableConstantException extends RuntimeException {
    private String nonExistentEnumValue;
    private String enumName;

    public NoSuchEnumerableConstantException(String nonExistentEnumValue, String enumName, String message) {
        super(message);
        this.nonExistentEnumValue = nonExistentEnumValue;
        this.enumName = enumName;
    }

    public NoSuchEnumerableConstantException(String nonExistentEnumValue, String enumName) {
        this(nonExistentEnumValue, enumName, String.format("%s '%s' does not exist", enumName, nonExistentEnumValue));
    }

    public String getNonExistentEnumValue() {
        return nonExistentEnumValue;
    }

    public String getEnum() {
        return enumName;
    }
}
