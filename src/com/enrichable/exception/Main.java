package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;

public class Main {

    public static void databaseQuery() {
        throw new EnrichableException(
                "DATABASE",
                "ERR1",
                "Failed to execute query: table 'users' not found",
                ErrorLevel.CRITICAL
        );
    }

    public static void userRepository() {
        try {
            databaseQuery();
        } catch (EnrichableException e) {
            e.addInformation(
                    "USER_REPOSITORY",
                    "ERR2",
                    "Failed to fetch user with ID: 1042",
                    ErrorLevel.ERROR
            ).log(); // chain it!
            e.addMetaData("userId", "1042")
                    .addMetaData("operation", "login")
                    .addMetaData("database", "users");
            throw e;
        }
    }

    public static void authService() {
        try {
            userRepository();
        } catch (EnrichableException e) {
            e.addInformation(
                    "AUTH_SERVICE",
                    "ERR3",
                    "Authentication failed — could not verify user identity",
                    ErrorLevel.WARNING
            );
            throw e;
        }
    }

    public static void loginController() {
        try {
            authService();
        } catch (EnrichableException e) {
            e.addInformation(
                    "LOGIN_CONTROLLER",
                    "ERR4",
                    "Login request failed for user — returning 500 to client",
                    ErrorLevel.WARNING
            );
            // e.log(); <- if you are against chaining
            throw e;
        }
    }

    public static void main(String[] args) {
        EnrichableException.setConfig(
                new ExceptionConfiguration()
                        .setShowTimestamp(true)
                        .setShowErrorLevel(true)
                        .setShowErrorCount(true)
                        .setShowMetadata(true)
        );
        try {
            loginController();
        } catch (EnrichableException e) {
            System.out.println(e);
        }
    }
}