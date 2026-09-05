package com.enrichable.logging;

import com.enrichable.config.ErrorLevel;
import com.enrichable.config.LogConfig;
import com.enrichable.model.EnrichInformation;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileEnrichLoggerTest {

    // ==================== Singleton ====================

    /**
     * Should return the same logger instance every time.
     */
    @Test
    void shouldReturnSameInstance() {
        assertSame(
                FileEnrichLogger.getInstance(),
                FileEnrichLogger.getInstance()
        );
    }

    // ==================== Basic Writing ====================

    /**
     * Should create the configured log file when writing a report.
     */
    @Test
    void shouldCreateLogFile() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString());

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            assertTrue(Files.exists(path));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should write the exception report to the configured file.
     */
    @Test
    void shouldWriteReportToFile() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("ENRICHABLE EXCEPTION REPORT"));
            assertTrue(content.contains("Database"));
            assertTrue(content.contains("DB-001"));
            assertTrue(content.contains("Connection failed"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include the total number of loggable errors in the report.
     */
    @Test
    void shouldIncludeTotalErrorCount() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true);

            EnrichInformation first = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation second = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Cache failed",
                    ErrorLevel.WARNING
            );

            FileEnrichLogger.getInstance().write(
                    List.of(first, second),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Total Errors : 2"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include the thrown timestamp when timestamp display is enabled.
     */
    @Test
    void shouldIncludeThrownTimestamp() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showTimestamp(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            String thrownAt = "2026-09-05 10:00:00";

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    thrownAt,
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Thrown At    : " + thrownAt));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    // ==================== Only Level Filtering ====================

    /**
     * Should write only errors matching the configured level.
     */
    @Test
    void shouldFilterByOnlyLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .onlyLevel(ErrorLevel.ERROR);

            EnrichInformation error = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation warning = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Cache failed",
                    ErrorLevel.WARNING
            );

            FileEnrichLogger.getInstance().write(
                    List.of(error, warning),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Database failed"));
            assertFalse(content.contains("Cache failed"));
            assertTrue(content.contains("Total Errors : 1"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include all errors when every error matches the configured level.
     */
    @Test
    void shouldIncludeAllErrorsWhenAllMatchOnlyLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .onlyLevel(ErrorLevel.ERROR);

            EnrichInformation first = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation second = new EnrichInformation(
                    "API",
                    "API-001",
                    "API failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(first, second),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Database failed"));
            assertTrue(content.contains("API failed"));
            assertTrue(content.contains("Total Errors : 2"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should produce a report with zero errors when no entry matches the configured level.
     */
    @Test
    void shouldWriteZeroErrorsWhenNothingMatchesOnlyLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .onlyLevel(ErrorLevel.CRITICAL);

            EnrichInformation first = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation second = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Cache failed",
                    ErrorLevel.WARNING
            );

            FileEnrichLogger.getInstance().write(
                    List.of(first, second),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Total Errors : 0"));
            assertFalse(content.contains("Database failed"));
            assertFalse(content.contains("Cache failed"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    // ==================== Minimum Level Filtering ====================

    /**
     * Should include errors at or above the configured minimum level.
     */
    @Test
    void shouldFilterByMinimumLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .minimumLevel(ErrorLevel.ERROR);

            EnrichInformation warning = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Cache warning",
                    ErrorLevel.WARNING
            );

            EnrichInformation error = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation critical = new EnrichInformation(
                    "System",
                    "SYS-001",
                    "System failure",
                    ErrorLevel.CRITICAL
            );

            FileEnrichLogger.getInstance().write(
                    List.of(warning, error, critical),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains("Cache warning"));
            assertTrue(content.contains("Database failed"));
            assertTrue(content.contains("System failure"));
            assertTrue(content.contains("Total Errors : 2"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include all errors when the minimum level is {@code INFO}.
     */
    @Test
    void shouldIncludeAllErrorsForInfoMinimumLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .minimumLevel(ErrorLevel.INFO);

            EnrichInformation info = new EnrichInformation(
                    "Application",
                    "APP-001",
                    "Information message",
                    ErrorLevel.INFO
            );

            EnrichInformation warning = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Cache warning",
                    ErrorLevel.WARNING
            );

            EnrichInformation error = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation critical = new EnrichInformation(
                    "System",
                    "SYS-001",
                    "System failure",
                    ErrorLevel.CRITICAL
            );

            FileEnrichLogger.getInstance().write(
                    List.of(info, warning, error, critical),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("Information message"));
            assertTrue(content.contains("Cache warning"));
            assertTrue(content.contains("Database failed"));
            assertTrue(content.contains("System failure"));
            assertTrue(content.contains("Total Errors : 4"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include only critical errors when the minimum level is {@code CRITICAL}.
     */
    @Test
    void shouldIncludeOnlyCriticalForCriticalMinimumLevel() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .minimumLevel(ErrorLevel.CRITICAL);

            EnrichInformation error = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Database failed",
                    ErrorLevel.ERROR
            );

            EnrichInformation critical = new EnrichInformation(
                    "System",
                    "SYS-001",
                    "System failure",
                    ErrorLevel.CRITICAL
            );

            FileEnrichLogger.getInstance().write(
                    List.of(error, critical),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains("Database failed"));
            assertTrue(content.contains("System failure"));
            assertTrue(content.contains("Total Errors : 1"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    // ==================== File Writing ====================

    /**
     * Should append the new report when clear-before-write is disabled.
     */
    @Test
    void shouldAppendWhenClearBeforeWriteIsDisabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            Files.writeString(path, "OLD CONTENT\n");

            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(false);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.startsWith("OLD CONTENT\n"));
            assertTrue(content.contains("Connection failed"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should clear existing content when clear-before-write is enabled.
     */
    @Test
    void shouldClearExistingContentBeforeWriting() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            Files.writeString(path, "OLD CONTENT\n");

            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains("OLD CONTENT"));
            assertTrue(content.contains("Connection failed"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should append multiple reports when clear-before-write remains disabled.
     */
    @Test
    void shouldAppendMultipleReports() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(false);

            EnrichInformation first = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "First failure",
                    ErrorLevel.ERROR
            );

            EnrichInformation second = new EnrichInformation(
                    "Cache",
                    "CACHE-001",
                    "Second failure",
                    ErrorLevel.WARNING
            );

            FileEnrichLogger.getInstance().write(
                    List.of(first),
                    "2026-09-05 10:00:00",
                    config
            );

            FileEnrichLogger.getInstance().write(
                    List.of(second),
                    "2026-09-05 10:05:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("First failure"));
            assertTrue(content.contains("Second failure"));
            assertTrue(content.indexOf("First failure")
                    < content.indexOf("Second failure"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    // ==================== Report Configuration ====================

    /**
     * Should hide the thrown timestamp when timestamp display is disabled.
     */
    @Test
    void shouldHideThrownTimestampWhenDisabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showTimestamp(false);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            String thrownAt = "2026-09-05 10:00:00";

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    thrownAt,
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains(thrownAt));
            assertFalse(content.contains("Thrown At"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should hide the error level when error level display is disabled.
     */
    @Test
    void shouldHideErrorLevelWhenDisabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showErrorLevel(false);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains("[ERROR]"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include the error level when error level display is enabled.
     */
    @Test
    void shouldShowErrorLevelWhenEnabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showErrorLevel(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("[ERROR]"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should hide metadata when metadata display is disabled.
     */
    @Test
    void shouldHideMetadataWhenDisabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showMetadata(false);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            information.addMetadata("host", "localhost");

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertFalse(content.contains("host"));
            assertFalse(content.contains("localhost"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include metadata when metadata display is enabled.
     */
    @Test
    void shouldShowMetadataWhenEnabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showMetadata(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            information.addMetadata("host", "localhost");

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("host"));
            assertTrue(content.contains("localhost"));
        } finally {
            Files.deleteIfExists(path);
        }
    }

    /**
     * Should include information timestamp when timestamp display is enabled.
     */
    @Test
    void shouldShowInformationTimestampWhenEnabled() throws Exception {
        Path path = Files.createTempFile("enrichable-test-", ".log");

        try {
            LogConfig config = new LogConfig()
                    .filePath(path.toString())
                    .clearBeforeWrite(true)
                    .showTimestamp(true);

            EnrichInformation information = new EnrichInformation(
                    "Database",
                    "DB-001",
                    "Connection failed",
                    ErrorLevel.ERROR
            );

            FileEnrichLogger.getInstance().write(
                    List.of(information),
                    "2026-09-05 10:00:00",
                    config
            );

            String content = Files.readString(path);

            assertTrue(content.contains("└─ Time : " + information.getDateTime()));
        } finally {
            Files.deleteIfExists(path);
        }
    }
}