package com.enrichable.annotation;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrichableHandlerTest {

    // ==================== Annotation Values ====================

    /**
     * Should preserve the configured {@code context}.
     */
    @Test
    void shouldPreserveContext() {
        EnrichableHandler annotation =
                ConfiguredHandler.class.getAnnotation(EnrichableHandler.class);

        assertEquals("USER_SERVICE", annotation.context());
    }

    /**
     * Should preserve the configured {@code defaultLevel}.
     */
    @Test
    void shouldPreserveConfiguredDefaultLevel() {
        EnrichableHandler annotation =
                ConfiguredHandler.class.getAnnotation(EnrichableHandler.class);

        assertEquals(ErrorLevel.WARNING, annotation.defaultLevel());
    }

    /**
     * Should use {@link ErrorLevel#ERROR} as the default level.
     */
    @Test
    void shouldUseErrorAsDefaultLevel() {
        EnrichableHandler annotation =
                DefaultHandler.class.getAnnotation(EnrichableHandler.class);

        assertEquals(ErrorLevel.ERROR, annotation.defaultLevel());
    }

    // ==================== Test Fixtures ====================

    @EnrichableHandler(
            context = "USER_SERVICE",
            defaultLevel = ErrorLevel.WARNING
    )
    static class ConfiguredHandler {}

    @EnrichableHandler(
            context = "DEFAULT_HANDLER"
    )
    static class DefaultHandler {}
}