package com.artlighter.glucosecontrolservice.general.exception;
/**
 * Исключение общего вида, выбрасываемое, когда создаваемый ресурс уже существует
 */

public class ResourceAlreadyExistsException extends RuntimeException {
    private Object resource;
    private Class<? extends Object> resourceType;

    public ResourceAlreadyExistsException(Object resource, String message) {
        super(message);
        this.resource = resource;
    }

    public ResourceAlreadyExistsException(Class<? extends Object> resourceType, String message) {
        super(message);
        this.resourceType = resourceType;
    }

    public Object getResource() {
        return resource;
    }

    public Class<? extends Object> getResourceType() {
        return resourceType;
    }
}
