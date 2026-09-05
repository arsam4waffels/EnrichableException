package com.enrichable.config;

public enum ErrorLevel {
    INFO,
    WARNING,
    ERROR,
    CRITICAL;

    public boolean isAtLeast(ErrorLevel minimumLevel) {
        return ordinal() >= minimumLevel.ordinal();
    }
}