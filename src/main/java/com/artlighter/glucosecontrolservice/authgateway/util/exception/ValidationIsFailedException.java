package com.artlighter.glucosecontrolservice.authgateway.util.exception;

import org.springframework.validation.Errors;

public class ValidationIsFailedException extends RuntimeException {
    private Errors errors;

    public ValidationIsFailedException(Errors errors, String message) {
        super(message);
        this.errors = errors;
    }

    public ValidationIsFailedException(Errors errors) {
        this(errors, "validation of request body failed");
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }
}
