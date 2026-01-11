package com.artlighter.glucosecontrolservice.auth.util.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    private Object resource;
    public ResourceAlreadyExistsException(Object resource, String message) {
        super(message);
        this.resource = resource;
    }
}
