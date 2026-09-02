package com.enrichable;

import com.enrichable.config.ErrorLevel;
import com.enrichable.config.LogConfig;
import com.enrichable.config.ConsoleConfig;
import com.enrichable.formatter.DefaultEnrichFormatter;
import com.enrichable.logging.FileEnrichLogger;
import com.enrichable.model.EnrichInformation;
import com.enrichable.validation.EnrichValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnrichableException extends RuntimeException {
    private volatile ConsoleConfig consoleConfig = new ConsoleConfig();
    private volatile LogConfig logConfig = new LogConfig();
    private final String thrownAt = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final List<EnrichInformation> informationList = new CopyOnWriteArrayList<>();
    public EnrichableException(String context,
                               String code,
                               String message,
                               ErrorLevel level,
                               Throwable cause) {
        super(message, cause);
        addInformation(context, code, message, level);
    }
    public EnrichableException setConsoleConfig(ConsoleConfig consoleConfig) {
        EnrichValidator.requireNonNull(consoleConfig, "Console configuration");
        this.consoleConfig = consoleConfig;
        return this;
    }
    public synchronized EnrichableException addInformation(String context,
                                              String code,
                                              String message,
                                              ErrorLevel level) {
        EnrichValidator.requireNonBlank(context, "Exception context");
        EnrichValidator.requireNonBlank(code, "Exception code");
        EnrichValidator.requireNonBlank(message, "Exception message");
        EnrichValidator.requireNonNull(level);
        informationList.add(new EnrichInformation(context, code, message, level));
        return this;
    }
    @Deprecated
    public synchronized EnrichableException addMetadata(String key, String value) {
        if (informationList.isEmpty())
            throw new IllegalStateException("Cannot add metadata without exception information.");
        informationList.getLast().addMetadata(
                EnrichValidator.normalizeMetadataKey(key),
                EnrichValidator.normalizeMetadataValue(value)
        );
        return this;
    }
    @Deprecated
    public EnrichableException onlyLog(ErrorLevel level) {
        EnrichValidator.requireNonNull(level);
        this.logConfig.onlyLevel(level);
        return this;
    }
    public void writeLog() {
        FileEnrichLogger.getInstance().write(
                informationList,
                thrownAt,
                logConfig);
    }
    @Override
    public synchronized String toString() {
        return new DefaultEnrichFormatter(consoleConfig).format(informationList);
    }
    public List<EnrichInformation> getInformationList() {
        return informationList;
    }
}