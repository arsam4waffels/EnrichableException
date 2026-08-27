package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnrichableExceptionTest {
    /**
     * Verifies that multiple pieces of exception information
     * can be stored inside a single EnrichableException. the chain... remember?
     *
     * The test checks both the original exception information
     * and the additional information added through addInformation().
     */
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
    /**
     * Verifies that the error level is included in the output
     * when error level display is enabled.
     */
    @Test
    void shouldShowErrorLevelWhenEnabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowErrorLevel(true)
        );
        String result = exception.toString();
        // CRITICAL should be visible when error level display is enabled.
        assertTrue(result.contains("[CRITICAL]"));
    }
    /**
     * Verifies that the error level is hidden when error level
     * display is disabled -> .setShowErrorLevel(false)
     */
    @Test
    void shouldHideErrorLevelWhenDisabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowErrorLevel(false)
        );
        String result = exception.toString();
        // CRITICAL should not appear when error level display is disabled.
        assertFalse(result.contains("[CRITICAL]"));
    }
    /**
     * Verifies that a null metadata key is rejected
     * instead of being silently accepted. (null's ain't slick)
     *
     * Metadata keys are required values, so null is invalid input.
     */
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
    /**
     * Verifies that a null metadata value is rejected
     * instead of being silently accepted.
     *
     * Metadata values are required key, so null is invalid input.
     */
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
    /**
     * Verifies that a blank metadata key is normalized to -> "BLANK".
     *
     * An empty key is not useful, but you have the freedom to do so.
     * Instead of rejecting it, I'll give it an explicit placeholder.
     */
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
        // Blank metadata keys are represented as -> [BLANK=value].
        assertTrue(result.contains("[BLANK=user-6969]"));
    }
    /**
     * Verifies that a blank metadata value is normalized to -> "BLANK".
     *
     * We keep the metadata entry instead of throwing an exception,
     * because blank values are allowed by our metadata contract.
     */
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
        // Blank metadata values are represented as [key=BLANK].
        assertTrue(result.contains("[userID=BLANK]"));
    }
    /**
     * Verifies that metadata is hidden when metadata display
     * is disabled in ExceptionConfiguration -> .setShowMetadata(false)
     *
     * The metadata may still exist internally; but here we only
     * check whether it is included in the textual output.
     */
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
    /**
     * Verifies that the error number is included in the output
     * when error count display is enabled.
     */
    @Test
    void shouldShowErrorCountWhenEnabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addInformation(
                        "AUTH_SERVICE",
                        "AUTH-001",
                        "Authentication failed",
                        ErrorLevel.WARNING
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowErrorCount(true)
        );
        String result = exception.toString();
        // Both errors should have their own sequential number.
        assertTrue(result.contains("[ERROR-1]"));
        assertTrue(result.contains("[ERROR-2]"));
    }
    /**
     * Verifies that error numbers are hidden when error count
     * display is disabled.
     */
    @Test
    void shouldHideErrorCountWhenDisabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                ).addInformation(
                        "AUTH_SERVICE",
                        "AUTH-001",
                        "Authentication failed",
                        ErrorLevel.WARNING
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowErrorCount(false)
        );
        String result = exception.toString();
        // Error numbers should not appear when counting is disabled.
        assertFalse(result.contains("[ERROR-1]"));
        assertFalse(result.contains("[ERROR-2]"));
    }
    /**
     * Verifies that the original exception cause is preserved
     * by EnrichableException.
     *
     * This ensures that the underlying exception is not lost when
     * additional information is attached to the enriched exception.
     */
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
        // The exact same cause instance should be preserved.
        assertSame(illegalStateException, exception.getCause());
    }
    /**
     * Verifies that a null exception context is rejected.
     *
     * The context identifies where the problem happened,
     * so allowing it to be null would make the error less informative.
     * And then it makes you question yourself... why are you even using this library?
     */
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
    /**
     * Verifies that the timestamp is included when timestamp
     * display is enabled.
     *
     * The exact timestamp cannot be predicted because it is generated
     * at runtime, so the test validates its format instead.
     */
    @Test
    void shouldShowTimestampWhenEnabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowTimestamp(true)
        );
        String result = exception.toString();
        /*
         * Regex breakdown (It destroyed me, it was like talking to an alien):
         *
         * (?s)       -> Enables DOTALL mode, allowing '.' to match line breaks.
         * .*         -> Match anything before the timestamp.
         * \\[        -> Match a literal '['.
         * \\d{4}     -> Exactly 4 digits (year).
         * -\\d{2}    -> 2 digits (month).
         * -\\d{2}    -> 2 digits (day).
         *             Format: YYYY-MM-DD
         * \\d{2}     -> 2 digits (hour).
         * :\\d{2}    -> 2 digits (minute).
         * :\\d{2}    -> 2 digits (second).
         *             Format: HH:mm:ss
         * \\]        -> Match a literal ']'.
         * .*         -> Match anything after the timestamp.
         *
         * In short:
         * [2026-08-26 12:49:59]
         *     ↑          ↑
         * This entire pattern validates the timestamp format.
         * (I'm fine :D)
         */
        assertTrue(result.matches("(?s).*\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\].*"));
    }
    /**
     * Verifies that the timestamp is not included when timestamp
     * display is disabled -> .setShowTimestamp(false)
     *
     * The same regular expression is used to detect a timestamp,
     * but this time we expect it to be absent.
     */
    @Test
    void shouldHideTimestampWhenDisabled() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowTimestamp(false)
        );
        String result = exception.toString();
        assertFalse(result.matches("(?s).*\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\].*"));
    }
    /**
     * Verifies that addInformation() returns the same exception instance,
     * allowing multiple pieces of information to be chained together.
     */
    @Test
    void shouldReturnSameExceptionInstance() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                );
        EnrichableException result =
                exception.addInformation(
                        "AUTH_SERVICE",
                        "AUTH-001",
                        "Authentication failed",
                        ErrorLevel.WARNING
                );
        // Fluent methods should return the same object, not create a new one.
        assertSame(exception, result);
    }
    @Test
    void shouldKeepMetadataAttachedToItsException() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                )
                        .addMetaData("userId", "1042")
                        .addInformation(
                                "AUTH",
                                "AUTH-001",
                                "Authentication failed",
                                ErrorLevel.WARNING
                        )
                        .addMetaData("userId", "2048");
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        String result = exception.toString();
        assertTrue(result.contains("[userId=1042]"));
        assertTrue(result.contains("[userId=2048]"));
    }
    @Test
    void shouldNotLeakMetadataBetweenExceptions() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                )
                        .addMetaData("userId", "1042")
                        .addInformation(
                                "AUTH",
                                "AUTH-001",
                                "Authentication failed",
                                ErrorLevel.WARNING
                        );
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        String result = exception.toString();
        int databaseStart = result.indexOf("[DATABASE:DB-001] Database failed");
        int authStart = result.indexOf("[AUTH:AUTH-001] Authentication failed");
        String databaseSection = result.substring(databaseStart, authStart);
        assertTrue(databaseSection.contains("[userId=1042]"));
        assertFalse(databaseSection.contains("2048"));
    }
    @Test
    void shouldSupportMultipleMetadataEntriesForOneException() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                )
                        .addMetaData("userId", "1042")
                        .addMetaData("requestId", "req-abc-999")
                        .addMetaData("retryCount", "3");
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        String result = exception.toString();
        assertTrue(result.contains("[userId=1042]"));
        assertTrue(result.contains("[requestId=req-abc-999]"));
        assertTrue(result.contains("[retryCount=3]"));
    }
    @Test
    void shouldRejectNullConfiguration() {
        EnrichableException exception =
                new EnrichableException(
                        "DATABASE",
                        "DB-001",
                        "Database failed",
                        ErrorLevel.CRITICAL,
                        null
                )
                        .addMetaData("userId", "1042")
                        .addMetaData("requestId", "req-abc-999")
                        .addMetaData("retryCount", "3");
        exception.setConfig(
                new ExceptionConfiguration()
                        .setShowMetadata(true)
        );
        assertThrows(IllegalArgumentException.class,
                () -> exception.setConfig(null));
    }
}