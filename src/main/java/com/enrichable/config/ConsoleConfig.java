package com.enrichable.config;

public class ConsoleConfig {
    private boolean showTimestamp = true;
    private boolean showErrorLevel = true;
    private boolean showErrorCount = true;
    private boolean showMetadata = true;
    public ConsoleConfig setShowTimestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }
    public boolean showTimestamp() {
        return this.showTimestamp;
    }
    public ConsoleConfig setShowErrorLevel(boolean showErrorLevel) {
        this.showErrorLevel = showErrorLevel;
        return this;
    }
    public boolean showErrorLevel() {
        return this.showErrorLevel;
    }
    public ConsoleConfig setShowErrorCount(boolean showErrorCount) {
        this.showErrorCount = showErrorCount;
        return this;
    }
    public boolean showErrorCount() {
        return this.showErrorCount;
    }
    public ConsoleConfig setShowMetadata(boolean showMetadata) {
        this.showMetadata = showMetadata;
        return this;
    }
    public boolean showMetadata() {
        return this.showMetadata;
    }
}
