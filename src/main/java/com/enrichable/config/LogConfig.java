package com.enrichable.config;

import com.enrichable.validation.EnrichValidator;

public class LogConfig {
    private ErrorLevel onlyLevel;
    private ErrorLevel minimumLevel;
    private boolean showTimestamp = true;
    private boolean showErrorLevel = true;
    private boolean showMetadata = true;
    private String filePath = "enrichable.log";
    private boolean clearBeforeWrite = false;
    public LogConfig onlyLevel(ErrorLevel level) {
        EnrichValidator.requireNonNull(level);
        this.onlyLevel = level;
        this.minimumLevel = null;
        return this;
    }
    public LogConfig minimumLevel(ErrorLevel level) {
        EnrichValidator.requireNonNull(level);
        this.minimumLevel = level;
        this.onlyLevel = null;
        return this;
    }
    public LogConfig showTimestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }
    public boolean showTimestamp() {
        return this.showTimestamp;
    }
    public LogConfig showErrorLevel(boolean showErrorLevel) {
        this.showErrorLevel = showErrorLevel;
        return this;
    }
    public boolean showErrorLevel() {
        return this.showErrorLevel;
    }
    public LogConfig showMetadata(boolean showMetadata) {
        this.showMetadata = showMetadata;
        return this;
    }
    public boolean showMetadata() {
        return this.showMetadata;
    }
    public LogConfig filePath(String filePath) {
        EnrichValidator.requireNonBlank(filePath, "Log file path");
        this.filePath = filePath;
        return this;
    }
    public String filePath() {
        return this.filePath;
    }
    public LogConfig clearBeforeWrite(boolean clearBeforeWrite) {
        this.clearBeforeWrite = clearBeforeWrite;
        return this;
    }
    public boolean clearBeforeWrite() {
        return this.clearBeforeWrite;
    }
    public ErrorLevel onlyLevel() {
        return this.onlyLevel;
    }
    public ErrorLevel minimumLevel() {
        return this.minimumLevel;
    }
}