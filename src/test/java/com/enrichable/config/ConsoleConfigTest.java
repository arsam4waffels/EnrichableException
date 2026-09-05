package com.enrichable.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleConfigTest {

    // ==================== Default Configuration ====================

    /**
     * Should enable timestamp display by default.
     */
    @Test
    void shouldEnableTimestampByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        assertTrue(config.showTimestamp());
    }

    /**
     * Should enable error level display by default.
     */
    @Test
    void shouldEnableErrorLevelByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        assertTrue(config.showTimestamp());
    }

    /**
     * Should enable error count display by default.
     */
    @Test
    void shouldEnableErrorCountByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        assertTrue(config.showTimestamp());
    }

    /**
     * Should enable metadata display by default.
     */
    @Test
    void shouldEnableMetadataByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        assertTrue(config.showTimestamp());
    }

    // ==================== Property Configuration ====================

    /**
     * Should update the timestamp display setting.
     */
    @Test
    void shouldUpdateTimestampDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        config.showTimestamp(false);

        assertFalse(config.showTimestamp());
    }

    /**
     * Should return the same configuration instance when setting timestamp display.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingTimestampDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        assertSame(config, config.showTimestamp(false));
    }

    /**
     * Should update the error level display setting.
     */
    @Test
    void shouldUpdateErrorLevelDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        config.showErrorLevel(false);

        assertFalse(config.showErrorLevel());
    }

    /**
     * Should return the same configuration instance when setting error level display.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingErrorLevelDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        assertSame(config, config.showErrorLevel(false));
    }

    /**
     * Should update the error count display setting.
     */
    @Test
    void shouldUpdateErrorCountDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        config.showErrorCount(false);

        assertFalse(config.showErrorCount());
    }

    /**
     * Should return the same configuration instance when setting error count display.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingErrorCountDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        assertSame(config, config.showErrorCount(false));
    }

    /**
     * Should update the metadata display setting.
     */
    @Test
    void shouldUpdateMetadataDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        config.showMetadata(false);

        assertFalse(config.showMetadata());
    }

    /**
     * Should return the same configuration instance when setting metadata display.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingMetadataDisplay() {
        ConsoleConfig config = new ConsoleConfig();

        assertSame(config, config.showMetadata(false));
    }
}
