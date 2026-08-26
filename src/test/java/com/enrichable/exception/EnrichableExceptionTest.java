package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnrichableExceptionTest {
    @Test
    void shouldAddInformation() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addInformation(
                        "AUTH_SERVICE",
                        "AUTH-001",
                        "Authentication failed",
                        ErrorLevel.WARNING
                );
        String result = exception.toString();
        assertTrue(result.contains("DATABASE"));
        assertTrue(result.contains("DB-001"));
        assertTrue(result.contains("AUTH_SERVICE"));
        assertTrue(result.contains("AUTH-001"));
    }
    @Test
    void shouldRejectNullMetadataKey() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addMetaData(null, "null")
        );
    }
    @Test
    void shouldRejectNullMetadataValue() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addMetaData("null", null)
        );
    }
    @Test
    void shouldConvertBlankMetadataKeyToBlank() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addMetaData("","user-6969");

        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        String result = exception.toString();
        assertTrue(result.contains("[BLANK=user-6969]"));
    }
    @Test
    void shouldConvertBlankMetadataValueToBlank() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addMetaData("userID","");

        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        String result = exception.toString();
        assertTrue(result.contains("[userID=BLANK]"));
    }
    @Test
    void shouldNotShowMetadataWhenDisabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database connection failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addMetaData("userID","1234");

        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(false)
        );
        String result = exception.toString();
        assertFalse(result.contains("[userID=1234]"));
    }
    @Test
    void shouldPreserveExceptionCause() {
        IllegalStateException illegalStateException =
                new IllegalStateException("Database connection failed.");
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database operation failed.",
                        ErrorLevel.CRITICAL,
                        illegalStateException
                );

        assertSame(illegalStateException, exception.getCause());
    }
    @Test
    void shouldRejectNullExceptionContext() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        null,
                        "null",
                        "Authentication failed",
                        ErrorLevel.WARNING
                )
        );
    }
}