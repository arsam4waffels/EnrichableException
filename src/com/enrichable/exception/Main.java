package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;

public class Main {

    public static void main(String[] args) {

        // Configure the exception before the chaos begins.
        ExceptionConfiguration configuration = new ExceptionConfiguration()
                .setShowTimestamp(true)
                .setShowErrorLevel(true)
                .setShowErrorCount(true)
                .setShowMetadata(true);

        EnrichableException.setConfig(configuration);

        // The original problem. The one that started all of this.
        Throwable databaseCause = new RuntimeException("Users table does not exist.");
        // Creating the main exception with its original cause.
        EnrichableException exception = new EnrichableException(
                "DATABASE",
                "DB-001",
                "Failed to execute database query.",
                ErrorLevel.CRITICAL,
                databaseCause
        );

        // Adding another error to the same exception.
        exception.addInformation(
                "USER_REPOSITORY",
                "REPO-002",
                "Failed to fetch user with ID: 1042.",
                ErrorLevel.ERROR
        );

        // And another one, because apparently one error wasn't enough.
        exception.addInformation(
                "AUTH_SERVICE",
                "AUTH-003",
                "Could not verify user identity.",
                ErrorLevel.WARNING
        );

        // Extra context for the whole exception.
        exception
                .addMetaData("userId", "1042")
                .addMetaData("operation", "login")
                .addMetaData("database", "users");

        // Show everything in the console.
        System.out.println(exception);

        // Save the same information to errors.log.
        exception.log();

        // The actual cause is still available through Java's exception chain.
        System.out.println("Original cause:");
        System.out.println(exception.getCause());
    }
}