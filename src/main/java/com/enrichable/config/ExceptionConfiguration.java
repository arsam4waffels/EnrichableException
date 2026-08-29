package com.enrichable.config;

public class ExceptionConfiguration {
    // By default: we pretend everything is fine.
    private boolean showTimestamp = false;
    // How bad is it? its optional btw.
    private boolean showErrorLevel = false;
    // In case there are more than one problem.
    private boolean showErrorCount = false;
    // In case you need a little bit more data
    private boolean showMetadata = false;

    public ExceptionConfiguration setShowTimestamp(boolean showTimestamp) {
        this.showTimestamp = showTimestamp;
        return this;
    }
    public boolean getShowTimestamp() {
        return this.showTimestamp;
    }
    public ExceptionConfiguration setShowErrorLevel(boolean showErrorLevel) {
        this.showErrorLevel = showErrorLevel;
        return this;
    }
    public boolean getShowErrorLevel() {
        return this.showErrorLevel;
    }
    public ExceptionConfiguration setShowErrorCount(boolean showErrorCount) {
        this.showErrorCount = showErrorCount;
        return this;
    }
    public boolean getShowErrorCount() {
        return this.showErrorCount;
    }
    public ExceptionConfiguration setShowMetadata(boolean showMetadata) {
        this.showMetadata = showMetadata;
        return this;
    }
    public boolean getShowMetadata() {
        return this.showMetadata;
    }
}
