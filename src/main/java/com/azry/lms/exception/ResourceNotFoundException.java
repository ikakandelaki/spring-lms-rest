package com.azry.lms.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private static final String NOT_FOUND_MSG = "%s not found with %s: '%s'";

    private final String resourceName;

    private final String fieldName;

    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format(NOT_FOUND_MSG, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
