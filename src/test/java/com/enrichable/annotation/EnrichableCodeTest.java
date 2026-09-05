package com.enrichable.annotation;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrichableCodeTest {

    // ==================== Annotation Values ====================

    /**
     * Should preserve the configured {@code code}.
     */
    @Test
    void shouldPreserveCode() {
        EnrichableCode annotation =
                ConfiguredCode.class.getAnnotation(EnrichableCode.class);

        assertEquals("ERR-001", annotation.code());
    }

    /**
     * Should preserve the configured {@code level}.
     */
    @Test
    void shouldPreserveConfiguredLevel() {
        EnrichableCode annotation =
                ConfiguredCode.class.getAnnotation(EnrichableCode.class);

        assertEquals(ErrorLevel.CRITICAL, annotation.level());
    }

    /**
     * Should use {@link ErrorLevel#ERROR} as the default level.
     */
    @Test
    void shouldUseErrorAsDefaultLevel() {
        EnrichableCode annotation =
                DefaultCode.class.getAnnotation(EnrichableCode.class);

        assertEquals(ErrorLevel.ERROR, annotation.level());
    }

    // ==================== Test Fixtures ====================

    @EnrichableCode(
            code = "ERR-001",
            level = ErrorLevel.CRITICAL
    )
    static class ConfiguredCode {}

    @EnrichableCode(
            code = "DEFAULT-001"
    )
    static class DefaultCode {}
}