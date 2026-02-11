package com.artlighter.glucosecontrolservice.general.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    private Object resource;
    public ResourceAlreadyExistsException(Object resource, String message) {
        super(message);
        this.resource = resource;
    }
}
