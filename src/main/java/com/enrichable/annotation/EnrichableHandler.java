package com.enrichable.annotation;

import com.enrichable.config.ErrorLevel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnrichableHandler {
    String context();
    ErrorLevel defaultLevel() default ErrorLevel.ERROR;
}