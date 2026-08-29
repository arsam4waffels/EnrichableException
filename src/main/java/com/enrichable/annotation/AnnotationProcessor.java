package com.enrichable.annotation;

import com.enrichable.EnrichableException;

public final class AnnotationProcessor {
    private AnnotationProcessor() {}
    public static EnrichableException processHandler(Class<?> clazz,
                                                     String code,
                                                     String message) {
        if (!clazz.isAnnotationPresent(EnrichableHandler.class))
            throw new IllegalArgumentException(
                    clazz.getSimpleName() + " is not annotated with @EnrichableHandler"
            );
        EnrichableHandler annotation = clazz.getAnnotation(EnrichableHandler.class);
        return new EnrichableException(
                annotation.context(),
                code,
                message,
                annotation.defaultLevel(),
                null
        );
    }
    public static EnrichableException processCode(Class<? extends EnrichableException> clazz,
                                                  String context,
                                                  String message) {
        if (!clazz.isAnnotationPresent(EnrichableCode.class))
            throw new IllegalArgumentException(
                    clazz.getSimpleName() + " is not annotated with @EnrichableCode"
            );
        EnrichableCode annotation = clazz.getAnnotation(EnrichableCode.class);
        return new EnrichableException(
                context,
                annotation.code(),
                message,
                annotation.level(),
                null
        );
    }
}