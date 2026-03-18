package com.artlighter.glucosecontrolservice.general.exception;

/**
 * Исключение общего вида, выбрасываемое, когда не был найден тот или иной ресурс и когда его ненахождение должно
 *  быть исключительной ситуацией
 */
public class ResourceNotFoundException extends RuntimeException {
    private Class<? extends Object> resourceType;

    public ResourceNotFoundException(Class<? extends Object> resourceType, String message) {
        super(message);
        this.resourceType = resourceType;
    }

    public Class<? extends Object> getResourceType() {
        return resourceType;
    }
}
