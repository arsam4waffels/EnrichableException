package com.enrichable.model;

import com.enrichable.config.ErrorLevel;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnrichInformationTest {

    // ==================== Construction ====================

    /**
     * Should preserve the context provided during construction.
     */
    @Test
    void shouldStoreContext() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        assertEquals("Database", information.getContext());
    }

    /**
     * Should preserve the code provided during construction.
     */
    @Test
    void shouldStoreCode() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        assertEquals("DB-001", information.getCode());
    }

    /**
     * Should preserve the message provided during construction.
     */
    @Test
    void shouldStoreMessage() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        assertEquals("Connection failed", information.getMessage());
    }

    /**
     * Should preserve the error level provided during construction.
     */
    @Test
    void shouldStoreErrorLevel() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.CRITICAL);

        assertEquals(ErrorLevel.CRITICAL, information.getErrorLevel());
    }

    /**
     * Should assign a creation date and time during construction.
     */
    @Test
    void shouldSetDateTime() {
        LocalDateTime before = LocalDateTime.now();

        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(information.getDateTime());
        assertFalse(information.getDateTime().isBefore(before));
        assertFalse(information.getDateTime().isAfter(after));
    }

    /**
     * Should allow a null code when no code is provided.
     */
    @Test
    void shouldAllowNullCode() {
        EnrichInformation information =
                new EnrichInformation("Database", null, "Connection failed", ErrorLevel.ERROR);

        assertNull(information.getCode());
    }

    // ==================== Metadata ====================

    /**
     * Should add metadata to the information.
     */
    @Test
    void shouldAddMetadata() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        information.addMetadata("host", "localhost");

        assertEquals("localhost", information.getMetadata().get("host"));
    }

    /**
     * Should overwrite the existing value when the same metadata key is added again.
     */
    @Test
    void shouldOverwriteMetadataWithSameKey() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        information.addMetadata("host", "localhost");
        information.addMetadata("host", "production");

        assertEquals("production", information.getMetadata().get("host"));
        assertEquals(1, information.getMetadata().size());
    }

    /**
     * Should return an unmodifiable metadata map.
     */
    @Test
    void shouldReturnUnmodifiableMetadata() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        information.addMetadata("host", "localhost");

        Map<String, String> metadata = information.getMetadata();

        assertThrows(
                UnsupportedOperationException.class,
                () -> metadata.put("port", "5432")
        );
    }

    /**
     * Should preserve all metadata entries when multiple entries are added.
     */
    @Test
    void shouldPreserveMultipleMetadataEntries() {
        EnrichInformation information =
                new EnrichInformation("Database", "DB-001", "Connection failed", ErrorLevel.ERROR);

        information.addMetadata("host", "localhost");
        information.addMetadata("port", "5432");
        information.addMetadata("database", "users");

        assertEquals(3, information.getMetadata().size());
        assertEquals("localhost", information.getMetadata().get("host"));
        assertEquals("5432", information.getMetadata().get("port"));
        assertEquals("users", information.getMetadata().get("database"));
    }
}