package com.enrichable.annotation;

import com.enrichable.EnrichableException;
import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationProcessorTest {

    // ==================== processHandler ====================

    /**
     * Should create an exception using the handler annotation configuration.
     */
    @Test
    void shouldProcessHandlerAnnotation() {
        EnrichableException exception = AnnotationProcessor.processHandler(
                TestHandler.class,
                "TEST-001",
                "Test message"
        );

        assertNotNull(exception);
        assertEquals("TEST", exception.getInformationList().getFirst().getContext());
        assertEquals("TEST-001", exception.getInformationList().getFirst().getCode());
        assertEquals("Test message", exception.getInformationList().getFirst().getMessage());
        assertEquals(ErrorLevel.WARNING,
                exception.getInformationList().getFirst().getErrorLevel());
    }

    /**
     * Should reject a class that is not annotated with {@link EnrichableHandler}.
     */
    @Test
    void shouldRejectClassWithoutHandlerAnnotation() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnnotationProcessor.processHandler(
                        UnannotatedHandler.class,
                        "TEST-001",
                        "Test message"
                )
        );

        assertEquals(
                "UnannotatedHandler is not annotated with @EnrichableHandler",
                exception.getMessage()
        );
    }

    // ==================== processCode ====================

    /**
     * Should create an exception using the code annotation configuration.
     */
    @Test
    void shouldProcessCodeAnnotation() {
        EnrichableException exception = AnnotationProcessor.processCode(
                TestCode.class,
                "TEST",
                "Test message"
        );

        assertNotNull(exception);
        assertEquals("TEST", exception.getInformationList().getFirst().getContext());
        assertEquals("ERR-001", exception.getInformationList().getFirst().getCode());
        assertEquals("Test message", exception.getInformationList().getFirst().getMessage());
        assertEquals(ErrorLevel.CRITICAL,
                exception.getInformationList().getFirst().getErrorLevel());
    }

    /**
     * Should reject a class that is not annotated with {@link EnrichableCode}.
     */
    @Test
    void shouldRejectClassWithoutCodeAnnotation() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AnnotationProcessor.processCode(
                        UnannotatedCode.class,
                        "TEST",
                        "Test message"
                )
        );

        assertEquals(
                "UnannotatedCode is not annotated with @EnrichableCode",
                exception.getMessage()
        );
    }

    // ==================== Test Fixtures ====================

    @EnrichableHandler(
            context = "TEST",
            defaultLevel = ErrorLevel.WARNING
    )
    static class TestHandler {}

    @EnrichableCode(
            code = "ERR-001",
            level = ErrorLevel.CRITICAL
    )
    static class TestCode extends EnrichableException {

        TestCode() {
            super("TEST", "TEST-001", "Test message", ErrorLevel.ERROR, null);
        }
    }

    static class UnannotatedHandler {}

    static class UnannotatedCode extends EnrichableException {
        UnannotatedCode() {
            super("TEST", "TEST-001", "Test message", ErrorLevel.ERROR, null);
        }
    }
}