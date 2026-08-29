package com.enrichable;

import com.enrichable.config.ErrorLevel;
import com.enrichable.config.EnrichConfiguration;
import com.enrichable.formatter.DefaultEnrichFormatter;
import com.enrichable.logging.FileEnrichLogger;
import com.enrichable.model.EnrichInformation;
import com.enrichable.validation.EnrichValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * Regular exceptions: "I failed."
 * EnrichableException: "Allow me to explain exactly how."
 */
public class EnrichableException extends RuntimeException {
    private EnrichConfiguration config = new EnrichConfiguration();
    private ErrorLevel logFilter;
    private final String thrownAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final List<EnrichInformation> informationList = new ArrayList<>();
    public EnrichableException(String context,
                               String code,
                               String message,
                               ErrorLevel level,
                               Throwable cause) {
        super(message, cause);
        addInformation(context, code, message, level);
    }
    public EnrichableException setConfig(EnrichConfiguration config) {
        EnrichValidator.requireNonNull(config, "Exception configuration");
        this.config = config;
        return this;
    }
    public EnrichableException addInformation(String context, String code,
                                              String message, ErrorLevel level) {
        EnrichValidator.requireNonBlank(context, "Exception context");
        EnrichValidator.requireNonBlank(code,    "Exception code");
        EnrichValidator.requireNonBlank(message, "Exception message");
        EnrichValidator.requireNonNull(level);
        informationList.add(new EnrichInformation(context, code, message, level));
        return this;
    }
    @Deprecated
    public EnrichableException addMetadata(String key, String value) {
        if (informationList.isEmpty())
            throw new IllegalStateException("Cannot add metadata without exception information.");
        informationList.getLast().addMetadata(
                normalizeKey(key),
                normalizeValue(value)
        );
        return this;
    }
    public EnrichableException onlyLog(ErrorLevel level) {
        EnrichValidator.requireNonNull(level);
        this.logFilter = level;
        return this;
    }
    public void writeLog() {
        List<EnrichInformation> loggable = getLoggable();
        new FileEnrichLogger().write(loggable, thrownAt);
    }
    @Override
    public String toString() {
        return new DefaultEnrichFormatter(config).format(informationList);
    }
    private List<EnrichInformation> getLoggable() {
        if (logFilter == null) return informationList;
        return informationList.stream()
                .filter(i -> i.getErrorLevel() == logFilter)
                .toList();
    }
    private String normalizeKey(String key) {
        if (key == null) throw new IllegalArgumentException("Metadata key cannot be null.");
        return key.isBlank() ? "BLANK" : key;
    }
    private String normalizeValue(String value) {
        if (value == null) throw new IllegalArgumentException("Metadata value cannot be null.");
        return value.isBlank() ? "BLANK" : value;
    }
}