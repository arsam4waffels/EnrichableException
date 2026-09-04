package com.enrichable.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorLevelTest {

    // ==================== Values ====================

    /**
     * Should contain all supported error levels.
     */
    @Test
    void shouldContainAllErrorLevels() {
        assertArrayEquals(
                new ErrorLevel[]{
                        ErrorLevel.INFO,
                        ErrorLevel.WARNING,
                        ErrorLevel.ERROR,
                        ErrorLevel.CRITICAL
                },
                ErrorLevel.values()
        );
    }

    /**
     * Should return the correct error level for each valid name.
     */
    @Test
    void shouldResolveErrorLevelByName() {
        assertEquals(ErrorLevel.INFO, ErrorLevel.valueOf("INFO"));
        assertEquals(ErrorLevel.WARNING, ErrorLevel.valueOf("WARNING"));
        assertEquals(ErrorLevel.ERROR, ErrorLevel.valueOf("ERROR"));
        assertEquals(ErrorLevel.CRITICAL, ErrorLevel.valueOf("CRITICAL"));
    }

    // ==================== Order ====================

    /**
     * Should preserve the expected severity order.
     */
    @Test
    void shouldPreserveSeverityOrder() {
        assertEquals(0, ErrorLevel.INFO.ordinal());
        assertEquals(1, ErrorLevel.WARNING.ordinal());
        assertEquals(2, ErrorLevel.ERROR.ordinal());
        assertEquals(3, ErrorLevel.CRITICAL.ordinal());
    }

    // ==================== Validation ====================

    /**
     * Should reject an invalid error level name.
     */
    @Test
    void shouldRejectInvalidErrorLevelName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ErrorLevel.valueOf("INVALID")
        );
    }
}