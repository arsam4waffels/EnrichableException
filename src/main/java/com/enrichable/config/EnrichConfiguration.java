package com.enrichable.config;

public class EnrichConfiguration {
    private boolean showTimestamp = false;
    private boolean showErrorLevel = false;
    private boolean showErrorCount = false;
    private boolean showMetadata = false;
    public EnrichConfiguration setShowTimestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }
    public boolean isShowTimestamp() {
        return this.showTimestamp;
    }
    public EnrichConfiguration setShowErrorLevel(boolean showErrorLevel) {
        this.showErrorLevel = showErrorLevel;
        return this;
    }
    public boolean isShowErrorLevel() {
        return this.showErrorLevel;
    }
    public EnrichConfiguration setShowErrorCount(boolean showErrorCount) {
        this.showErrorCount = showErrorCount;
        return this;
    }
    public boolean isShowErrorCount() {
        return this.showErrorCount;
    }
    public EnrichConfiguration setShowMetadata(boolean showMetadata) {
        this.showMetadata = showMetadata;
        return this;
    }
    public boolean isShowMetadata() {
        return this.showMetadata;
    }
}
