package com.artlighter.glucosecontrolservice.auth.util.exception;

public class ResourceNotFoundException extends RuntimeException {
    private Object resource;
    public ResourceNotFoundException(Object resource, String message) {
        super(message);
        this.resource = resource;
    }
}
