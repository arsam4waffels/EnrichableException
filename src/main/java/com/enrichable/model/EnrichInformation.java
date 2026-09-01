package com.enrichable.model;

import com.enrichable.config.ErrorLevel;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnrichInformation {
    private final String context;
    private final String code;
    private final String message;
    private final LocalDateTime dateTime;
    private final ErrorLevel errorLevel;
    private final Map<String, String> metadata = new ConcurrentHashMap<>();
    public EnrichInformation(String context,
                                String code,
                                String message,
                                ErrorLevel errorLevel) {
        this.context = context;
        this.code = code;
        this.message = message;
        this.errorLevel = errorLevel;
        this.dateTime = LocalDateTime.now();
    }
    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }
    public String getContext() {
        return context;
    }
    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public ErrorLevel getErrorLevel() {
        return errorLevel;
    }
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
}