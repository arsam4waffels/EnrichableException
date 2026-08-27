package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;

public class Main {
    public static void main(String[] args) {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Connection failed",
                        ErrorLevel.CRITICAL,
                        null
                )
                        .addMetaData("userID", "1234")
                        .addMetaData("requestID", "req-abc-999")
                        .addInformation(
                                "CACHE",
                                "CACHE-004",
                                "Cache was unavailable",
                                ErrorLevel.WARNING
                        )
                        .addMetaData("cacheName", "user-cache");

        exception.writeLog();
    }
}