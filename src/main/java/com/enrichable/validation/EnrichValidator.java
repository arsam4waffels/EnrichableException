package com.enrichable.validation;

import com.enrichable.config.ErrorLevel;

public final class EnrichValidator {
    private EnrichValidator() {}
    public static void requireNonBlank(String value, String fieldName) {
        if (value == null)
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        if (value.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
    }
    public static void requireNonNull(ErrorLevel errorLevel) {
        if (errorLevel == null)
            throw new IllegalArgumentException("Error level cannot be null.");
    }
    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null)
            throw new IllegalArgumentException(fieldName + " cannot be null.");
    }
}