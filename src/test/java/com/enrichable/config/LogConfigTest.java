package com.enrichable.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogConfigTest {

    // ==================== Default Configuration ====================

    /**
     * Should have no exact error level filter by default.
     */
    @Test
    void shouldHaveNoOnlyLevelByDefault() {
        LogConfig config = new LogConfig();

        assertNull(config.onlyLevel());
    }

    /**
     * Should have no minimum error level filter by default.
     */
    @Test
    void shouldHaveNoMinimumLevelByDefault() {
        LogConfig config = new LogConfig();

        assertNull(config.minimumLevel());
    }

    /**
     * Should enable timestamp display by default.
     */
    @Test
    void shouldEnableTimestampByDefault() {
        LogConfig config = new LogConfig();

        assertTrue(config.showTimestamp());
    }

    /**
     * Should enable error level display by default.
     */
    @Test
    void shouldEnableErrorLevelByDefault() {
        LogConfig config = new LogConfig();

        assertTrue(config.showErrorLevel());
    }

    /**
     * Should enable metadata display by default.
     */
    @Test
    void shouldEnableMetadataByDefault() {
        LogConfig config = new LogConfig();

        assertTrue(config.showMetadata());
    }

    /**
     * Should use the default log file path.
     */
    @Test
    void shouldUseDefaultFilePath() {
        LogConfig config = new LogConfig();

        assertEquals("enrichable.log", config.filePath());
    }

    /**
     * Should disable clearing the log file before writing by default.
     */
    @Test
    void shouldDisableClearBeforeWriteByDefault() {
        LogConfig config = new LogConfig();

        assertFalse(config.clearBeforeWrite());
    }

    // ==================== Level Filtering ====================

    /**
     * Should set the exact error level filter.
     */
    @Test
    void shouldSetOnlyLevel() {
        LogConfig config = new LogConfig();

        config.onlyLevel(ErrorLevel.WARNING);

        assertEquals(ErrorLevel.WARNING, config.onlyLevel());
    }

    /**
     * Should return the same configuration instance when setting the exact error level filter.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingOnlyLevel() {
        LogConfig config = new LogConfig();

        assertSame(config, config.onlyLevel(ErrorLevel.WARNING));
    }

    /**
     * Should set the minimum error level filter.
     */
    @Test
    void shouldSetMinimumLevel() {
        LogConfig config = new LogConfig();

        config.minimumLevel(ErrorLevel.WARNING);

        assertEquals(ErrorLevel.WARNING, config.minimumLevel());
    }

    /**
     * Should return the same configuration instance when setting the minimum error level filter.
     */
    @Test
    void shouldReturnSameInstanceWhenSettingMinimumLevel() {
        LogConfig config = new LogConfig();

        assertSame(config, config.minimumLevel(ErrorLevel.WARNING));
    }

    /**
     * Should clear the minimum level when setting an exact error level filter.
     */
    @Test
    void shouldClearMinimumLevelWhenSettingOnlyLevel() {
        LogConfig config = new LogConfig();

        config.minimumLevel(ErrorLevel.INFO);
        config.onlyLevel(ErrorLevel.ERROR);

        assertNull(config.minimumLevel());
        assertEquals(ErrorLevel.ERROR, config.onlyLevel());
    }

    /**
     * Should clear the exact level when setting a minimum error level filter.
     */
    @Test
    void shouldClearOnlyLevelWhenSettingMinimumLevel() {
        LogConfig config = new LogConfig();

        config.onlyLevel(ErrorLevel.ERROR);
        config.minimumLevel(ErrorLevel.WARNING);

        assertNull(config.onlyLevel());
        assertEquals(ErrorLevel.WARNING, config.minimumLevel());
    }

    // ==================== Validation ====================

    /**
     * Should reject a {@code null} exact error level filter.
     */
    @Test
    void shouldRejectNullOnlyLevel() {
        LogConfig config = new LogConfig();

        assertThrows(
                IllegalArgumentException.class,
                () -> config.onlyLevel(null)
        );
    }

    /**
     * Should reject a {@code null} minimum error level filter.
     */
    @Test
    void shouldRejectNullMinimumLevel() {
        LogConfig config = new LogConfig();

        assertThrows(
                IllegalArgumentException.class,
                () -> config.minimumLevel(null)
        );
    }

    /**
     * Should reject a {@code null} log file path.
     */
    @Test
    void shouldRejectNullFilePath() {
        LogConfig config = new LogConfig();

        assertThrows(
                IllegalArgumentException.class,
                () -> config.filePath(null)
        );
    }

    /**
     * Should reject an empty log file path.
     */
    @Test
    void shouldRejectEmptyFilePath() {
        LogConfig config = new LogConfig();

        assertThrows(
                IllegalArgumentException.class,
                () -> config.filePath("")
        );
    }

    /**
     * Should reject a {@code blank} log file path.
     */
    @Test
    void shouldRejectBlankFilePath() {
        LogConfig config = new LogConfig();

        assertThrows(
                IllegalArgumentException.class,
                () -> config.filePath("   ")
        );
    }
}
