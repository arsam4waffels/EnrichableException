package com.enrichable;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrichableExceptionTest {
    /*
    * Builder
    */

    // Should build an exception with the required context and message.
    @Test
    void shouldBuildExceptionWithRequiredFields() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertNotNull(exception);
        assertEquals("User not found", exception.getMessage());
    }

    // Should use ERROR as the default error level when no level is specified.
    @Test
    void shouldUseErrorAsDefaultLevel() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertEquals(
                ErrorLevel.ERROR,
                exception.getInformationList().getFirst().getErrorLevel()
        );
    }

    // Should use the specified error level.
    @Test
    void shouldUseSpecifiedErrorLevel() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "Something went wrong"
                )
                        .level(ErrorLevel.CRITICAL)
                        .build();

        assertEquals(
                ErrorLevel.CRITICAL,
                exception.getInformationList().getFirst().getErrorLevel()
        );
    }

    // Should preserve the specified error code.
    @Test
    void shouldPreserveSpecifiedCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                )
                        .code("USER_404")
                        .build();

        assertEquals(
                "USER_404",
                exception.getInformationList().getFirst().getCode()
        );
    }

    // Should allow building an exception without specifying an error code.
    @Test
    void shouldAllowMissingCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertNull(
                exception.getInformationList().getFirst().getCode()
        );
    }

    // Should preserve the specified cause.
    @Test
    void shouldPreserveSpecifiedCause() {
        Throwable cause = new RuntimeException("Database connection failed");

        EnrichableException exception =
                new EnrichableException.Builder(
                        "DatabaseService",
                        "Database operation failed"
                )
                        .cause(cause)
                        .build();

        assertSame(cause, exception.getCause());
    }

    // Should allow building an exception without specifying a cause.
    @Test
    void shouldAllowMissingCause() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertNull(exception.getCause());
    }

    // Should return the same builder instance when setting a code.
    @Test
    void shouldReturnSameBuilderWhenSettingCode() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertSame(builder, builder.code("USER_404"));
    }

    // Should return the same builder instance when setting an error level.
    @Test
    void shouldReturnSameBuilderWhenSettingLevel() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertSame(builder, builder.level(ErrorLevel.WARNING));
    }

    // Should return the same builder instance when setting a cause.
    @Test
    void shouldReturnSameBuilderWhenSettingCause() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        Throwable cause = new RuntimeException();

        assertSame(builder, builder.cause(cause));
    }

    // Should create independent exceptions when building multiple times.
    @Test
    void shouldCreateIndependentExceptionsWhenBuildingMultipleTimes() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        EnrichableException first = builder
                .code("USER_404")
                .build();

        EnrichableException second = builder
                .code("USER_500")
                .build();

        assertEquals(
                "USER_404",
                first.getInformationList().getFirst().getCode()
        );

        assertEquals(
                "USER_500",
                second.getInformationList().getFirst().getCode()
        );

        assertNotSame(first, second);
    }
}