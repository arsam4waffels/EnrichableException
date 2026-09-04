package com.enrichable;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrichableExceptionTest {
    /*
    * Builder
    */

    /**
     * Should build an exception with the required context and message.
     * */
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

    /**
     * Should use {@code ERROR} as the default error level when no level is specified.
     * */
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

    /**
     * Should use the specified error level.
     */
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

    /**
     * Should preserve the specified error code.
     */
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

    /**
     * Should allow building an exception without specifying an error code.
     */
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

    /**
     * Should preserve the specified cause.
     */
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

    /**
     * Should allow building an exception without specifying a cause.
     */
    @Test
    void shouldAllowMissingCause() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertNull(exception.getCause());
    }

    /**
     * Should return the same builder instance when setting a code.
     */
    @Test
    void shouldReturnSameBuilderWhenSettingCode() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertSame(builder, builder.code("USER_404"));
    }

    /**
     * Should return the same builder instance when setting an error level.
     */
    @Test
    void shouldReturnSameBuilderWhenSettingLevel() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertSame(builder, builder.level(ErrorLevel.WARNING));
    }

    /**
     * Should return the same builder instance when setting a cause.
     */
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

    /**
     * Should create independent exceptions when building multiple times.
     */
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

    // ==================== Builder Validation ====================

    /**
     * Should reject a {@code null} context
     * */
    @Test
    void shouldRejectNullContext() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        null,
                        "User not found"
                )
        );
    }

    /**
     * Should reject a {@code blank} context
     * */
    @Test
    void shouldRejectBlankContext() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        "   ",
                        "User not found"
                )
        );
    }

    /**
     * Should reject an {@code empty} context
     * */
    @Test
    void shouldRejectEmptyContext() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        "",
                        "User not found"
                )
        );
    }

    /**
     * Should reject a {@code null} message
     * */
    @Test
    void shouldRejectNullMessage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        "UserService",
                        null
                )
        );
    }

    /**
     * Should reject a {@code blank} message
     * */
    @Test
    void shouldRejectBlankMessage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        "UserService",
                        "   "
                )
        );
    }

    /**
     * Should reject an {@code empty} message
     * */
    @Test
    void shouldRejectEmptyMessage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EnrichableException.Builder(
                        "UserService",
                        ""
                )
        );
    }

    /**
     * Should reject a {@code null} code when explicitly specified.
     * */
    @Test
    void shouldRejectNullCode() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.code(null)
        );
    }

    /**
     * Should reject an {@code empty} error code when explicitly specified.
     * */
    @Test
    void shouldRejectEmptyCode() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.code("")
        );
    }

    /**
     * Should reject a {@code blank} error code when explicitly specified.
     * */
    @Test
    void shouldRejectBlankCode() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.code("   ")
        );
    }

    /**
     * Should reject a {@code null} error level when explicitly specified.
     * */
    @Test
    void shouldRejectNullLevel() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.level(null)
        );
    }

    /**
     * Should reject a {@code null} cause when explicitly specified.
     * */
    @Test
    void shouldRejectNullCause() {
        EnrichableException.Builder builder =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.cause(null)
        );
    }

    // ==================== Information ====================

    /**
     * Should contain the initial information created by the builder.
     */
    @Test
    void shouldContainInitialInformation() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                )
                        .code("USER_404")
                        .level(ErrorLevel.WARNING)
                        .build();

        assertEquals(1, exception.getInformationList().size());

        var information = exception.getInformationList().getFirst();

        assertEquals("UserService", information.getContext());
        assertEquals("USER_404", information.getCode());
        assertEquals("User not found", information.getMessage());
        assertEquals(ErrorLevel.WARNING, information.getErrorLevel());
    }

    /**
     * Should add a new information entry to the exception.
     */
    @Test
    void shouldAddInformation() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        exception.addInformation(
                "DatabaseService",
                "DB_001",
                "Database connection failed",
                ErrorLevel.CRITICAL
        );

        assertEquals(2, exception.getInformationList().size());

        var information = exception.getInformationList().getLast();

        assertEquals("DatabaseService", information.getContext());
        assertEquals("DB_001", information.getCode());
        assertEquals("Database connection failed", information.getMessage());
        assertEquals(ErrorLevel.CRITICAL, information.getErrorLevel());
    }

    /**
     * Should allow adding information without an error code.
     */
    @Test
    void shouldAllowInformationWithoutCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        exception.addInformation(
                "DatabaseService",
                null,
                "Database connection failed",
                ErrorLevel.ERROR
        );

        var information = exception.getInformationList().getLast();

        assertNull(information.getCode());
    }

    /**
     * Should preserve the order in which information entries are added.
     */
    @Test
    void shouldPreserveInformationOrder() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        exception.addInformation(
                "DatabaseService",
                "DB_001",
                "Database failed",
                ErrorLevel.ERROR
        );

        exception.addInformation(
                "PaymentService",
                "PAY_001",
                "Payment failed",
                ErrorLevel.WARNING
        );

        assertEquals(
                "UserService",
                exception.getInformationList().get(0).getContext()
        );

        assertEquals(
                "DatabaseService",
                exception.getInformationList().get(1).getContext()
        );

        assertEquals(
                "PaymentService",
                exception.getInformationList().get(2).getContext()
        );
    }

    /**
     * Should return the same exception instance after adding information.
     */
    @Test
    void shouldReturnSameExceptionWhenAddingInformation() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        EnrichableException result = exception.addInformation(
                "DatabaseService",
                "DB_001",
                "Database failed",
                ErrorLevel.ERROR
        );

        assertSame(exception, result);
    }

    // ==================== Information Validation ====================

    /**
     * Should reject a {@code null} context when adding information.
     */
    @Test
    void shouldRejectNullInformationContext() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        null,
                        "USER_404",
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject an empty context when adding information.
     */
    @Test
    void shouldRejectEmptyInformationContext() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "",
                        "USER_404",
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject a {@code blank} context when adding information.
     */
    @Test
    void shouldRejectBlankInformationContext() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "   ",
                        "USER_404",
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject a {@code null} message when adding information.
     */
    @Test
    void shouldRejectNullInformationMessage() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "USER_404",
                        null,
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject an empty message when adding information.
     */
    @Test
    void shouldRejectEmptyInformationMessage() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "USER_404",
                        "",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject a {@code blank} message when adding information.
     */
    @Test
    void shouldRejectBlankInformationMessage() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "USER_404",
                        "   ",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject a {@code null} error level when adding information.
     */
    @Test
    void shouldRejectNullInformationLevel() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "USER_404",
                        "User not found",
                        null
                )
        );
    }

    /**
     * Should reject an empty error code when adding information.
     */
    @Test
    void shouldRejectEmptyInformationCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "",
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should reject a {@code blank} error code when adding information.
     */
    @Test
    void shouldRejectBlankInformationCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                IllegalArgumentException.class,
                () -> exception.addInformation(
                        "UserService",
                        "   ",
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    /**
     * Should allow a {@code null} error code when adding information.
     */
    @Test
    void shouldAllowNullInformationCode() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertDoesNotThrow(
                () -> exception.addInformation(
                        "UserService",
                        null,
                        "User not found",
                        ErrorLevel.ERROR
                )
        );
    }

    // ==================== Information List ====================

    /**
     * Should return an unmodifiable information list.
     */
    @Test
    void shouldReturnUnmodifiableInformationList() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        assertThrows(
                UnsupportedOperationException.class,
                () -> exception.getInformationList().clear()
        );
    }

    /**
     * Should preserve existing information when modification of the returned list is attempted.
     */
    @Test
    void shouldPreserveInformationAfterModificationAttempt() {
        EnrichableException exception =
                new EnrichableException.Builder(
                        "UserService",
                        "User not found"
                ).build();

        var informationList = exception.getInformationList();

        assertThrows(
                UnsupportedOperationException.class,
                () -> informationList.removeFirst()
        );

        assertEquals(1, exception.getInformationList().size());
    }
}