package com.enrichable.validation;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrichValidatorTest {

    // ==================== requireNonBlank ====================

    /**
     * Should accept a non-blank value.
     */
    @Test
    void shouldAcceptNonBlankValue() {
        assertDoesNotThrow(() ->
                EnrichValidator.requireNonBlank("Database", "context")
        );
    }

    /**
     * Should reject a null value.
     */
    @Test
    void shouldRejectNullValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonBlank(null, "context")
        );

        assertEquals("context cannot be null.", exception.getMessage());
    }

    /**
     * Should reject an empty value.
     */
    @Test
    void shouldRejectEmptyValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonBlank("", "context")
        );

        assertEquals("context cannot be blank.", exception.getMessage());
    }

    /**
     * Should reject a blank value containing only spaces.
     */
    @Test
    void shouldRejectBlankValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonBlank("   ", "context")
        );

        assertEquals("context cannot be blank.", exception.getMessage());
    }

    /**
     * Should reject a blank value containing only tabs.
     */
    @Test
    void shouldRejectTabOnlyValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonBlank("\t\t", "context")
        );

        assertEquals("context cannot be blank.", exception.getMessage());
    }

    /**
     * Should include the provided field name in the validation message.
     */
    @Test
    void shouldIncludeFieldNameInValidationMessage() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonBlank(null, "message")
        );

        assertEquals("message cannot be null.", exception.getMessage());
    }

    // ==================== requireNonNull ====================

    /**
     * Should accept a non-null error level.
     */
    @Test
    void shouldAcceptNonNullErrorLevel() {
        assertDoesNotThrow(() ->
                EnrichValidator.requireNonNull(ErrorLevel.ERROR)
        );
    }

    /**
     * Should reject a null error level.
     */
    @Test
    void shouldRejectNullErrorLevel() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonNull((ErrorLevel) null)
        );

        assertEquals("Error level cannot be null.", exception.getMessage());
    }

    /**
     * Should accept a non-null object.
     */
    @Test
    void shouldAcceptNonNullObject() {
        assertDoesNotThrow(() ->
                EnrichValidator.requireNonNull(new Object(), "configuration")
        );
    }

    /**
     * Should reject a null object.
     */
    @Test
    void shouldRejectNullObject() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonNull(null, "configuration")
        );

        assertEquals("configuration cannot be null.", exception.getMessage());
    }

    /**
     * Should include the provided field name in the validation message.
     */
    @Test
    void shouldIncludeFieldNameForNullObject() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.requireNonNull(null, "cause")
        );

        assertEquals("cause cannot be null.", exception.getMessage());
    }

    // ==================== normalizeMetadataKey ====================

    /**
     * Should preserve a non-blank metadata key.
     */
    @Test
    void shouldPreserveNonBlankMetadataKey() {
        assertEquals(
                "userId",
                EnrichValidator.normalizeMetadataKey("userId")
        );
    }

    /**
     * Should replace a blank metadata key with {@code BLANK}.
     */
    @Test
    void shouldNormalizeBlankMetadataKey() {
        assertEquals(
                "BLANK",
                EnrichValidator.normalizeMetadataKey("   ")
        );
    }

    /**
     * Should replace an empty metadata key with {@code BLANK}.
     */
    @Test
    void shouldNormalizeEmptyMetadataKey() {
        assertEquals(
                "BLANK",
                EnrichValidator.normalizeMetadataKey("")
        );
    }

    /**
     * Should reject a null metadata key.
     */
    @Test
    void shouldRejectNullMetadataKey() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.normalizeMetadataKey(null)
        );

        assertEquals("Metadata key cannot be null.", exception.getMessage());
    }


// ==================== normalizeMetadataValue ====================

    /**
     * Should preserve a non-blank metadata value.
     */
    @Test
    void shouldPreserveNonBlankMetadataValue() {
        assertEquals(
                "Arsam",
                EnrichValidator.normalizeMetadataValue("Arsam")
        );
    }

    /**
     * Should replace a blank metadata value with {@code BLANK}.
     */
    @Test
    void shouldNormalizeBlankMetadataValue() {
        assertEquals(
                "BLANK",
                EnrichValidator.normalizeMetadataValue("   ")
        );
    }

    /**
     * Should replace an empty metadata value with {@code BLANK}.
     */
    @Test
    void shouldNormalizeEmptyMetadataValue() {
        assertEquals(
                "BLANK",
                EnrichValidator.normalizeMetadataValue("")
        );
    }

    /**
     * Should reject a null metadata value.
     */
    @Test
    void shouldRejectNullMetadataValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EnrichValidator.normalizeMetadataValue(null)
        );

        assertEquals("Metadata value cannot be null.", exception.getMessage());
    }
}
