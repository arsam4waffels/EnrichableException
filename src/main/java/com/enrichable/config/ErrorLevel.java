package com.enrichable.config;

public enum ErrorLevel {
    INFO,
    WARNING,
    ERROR,
    CRITICAL;

    /**
     * Checks whether this error level is at least as severe as the given level.
     *
     * @param minimumLevel the minimum severity level
     * @return {@code true} if this level meets or exceeds the given level
     */
    public boolean isAtLeast(ErrorLevel minimumLevel) {
        return ordinal() >= minimumLevel.ordinal();
    }
}