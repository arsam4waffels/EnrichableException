package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;

public class Main {
    public static void main(String[] args) {
        // Database error
        EnrichableException databaseError =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Failed to execute query: table 'users' not found",
                        ErrorLevel.CRITICAL,
                        new IllegalStateException("Table 'users' does not exist.")
                );
        databaseError
                .addMetaData("userId", "1042")
                .addMetaData("query", "SELECT * FROM users")
                .addMetaData("retryCount", "3");
        databaseError.setConfig(
                new ExceptionConfiguration()
                        .setShowTimestamp(true)
                        .setShowErrorLevel(true)
                        .setShowErrorCount(true)
                        .setShowMetadata(true)
        );
        System.out.println("DATABASE ERROR");
        System.out.println(databaseError);
        databaseError.log();
        // Authentication error
        EnrichableException authenticationError =
                new EnrichableException(
                        "AUTH_SERVICE",
                        "AUTH-001",
                        "Authentication failed",
                        ErrorLevel.WARNING,
                        null
                );
        authenticationError
                .addMetaData("userId", "1042")
                .addMetaData("method", "PASSWORD");
        authenticationError.setConfig(
                new ExceptionConfiguration()
                        .setShowTimestamp(true)
                        .setShowErrorLevel(true)
                        .setShowMetadata(true)
        );
        System.out.println("AUTHENTICATION ERROR");
        System.out.println(authenticationError);
    }
}