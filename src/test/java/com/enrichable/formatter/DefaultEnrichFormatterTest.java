package com.enrichable.formatter;

import com.enrichable.config.ConsoleConfig;
import com.enrichable.config.ErrorLevel;
import com.enrichable.model.EnrichInformation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEnrichFormatterTest {

    // ==================== Basic Formatting ====================

    /**
     * Should produce a non-blank formatted result.
     */
    @Test
    void shouldProduceNonBlankOutput() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertNotNull(result);
        assertFalse(result.isBlank());
    }

    /**
     * Should include the error count in the formatted output by default.
     */
    @Test
    void shouldIncludeErrorCountByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("[1-ERRORS]"));
    }

    /**
     * Should include the context in the formatted output.
     */
    @Test
    void shouldIncludeContext() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("Database"));
    }

    /**
     * Should include the code in the formatted output when specified.
     */
    @Test
    void shouldIncludeCode() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("DB-001"));
    }

    /**
     * Should include the message in the formatted output.
     */
    @Test
    void shouldIncludeMessage() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("Connection failed"));
    }

    /**
     * Should include the error level in the formatted output by default.
     */
    @Test
    void shouldIncludeErrorLevelByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("[ERROR]"));
    }

    /**
     * Should include the timestamp in the formatted output by default.
     */
    @Test
    void shouldIncludeTimestampByDefault() {
        ConsoleConfig config = new ConsoleConfig();

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("[" + information.getDateTime().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ) + "]"));
    }

    // ==================== Console Configuration ====================

    /**
     * Should hide the error count when error count display is disabled.
     */
    @Test
    void shouldHideErrorCountWhenDisabled() {
        ConsoleConfig config = new ConsoleConfig()
                .showErrorCount(false);

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertFalse(result.contains("[ERROR-1]"));
    }

    /**
     * Should hide the timestamp when timestamp display is disabled.
     */
    @Test
    void shouldHideTimestampWhenDisabled() {
        ConsoleConfig config = new ConsoleConfig()
                .showTimestamp(false);

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        String timestamp = information.getDateTime().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        assertFalse(result.contains(timestamp));
    }

    /**
     * Should hide the error level when error level display is disabled.
     */
    @Test
    void shouldHideErrorLevelWhenDisabled() {
        ConsoleConfig config = new ConsoleConfig()
                .showErrorLevel(false);

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        String result = formatter.format(List.of(information));

        assertFalse(result.contains("[ERROR]"));
    }

    /**
     * Should hide metadata when metadata display is disabled.
     */
    @Test
    void shouldHideMetadataWhenDisabled() {
        ConsoleConfig config = new ConsoleConfig()
                .showMetadata(false);

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        information.addMetadata("host", "localhost");

        String result = formatter.format(List.of(information));

        assertFalse(result.contains("[host=localhost]"));
    }

    /**
     * Should display metadata when metadata display is enabled.
     */
    @Test
    void shouldShowMetadataWhenEnabled() {
        ConsoleConfig config = new ConsoleConfig()
                .showMetadata(true);

        DefaultEnrichFormatter formatter =
                new DefaultEnrichFormatter(config);

        EnrichInformation information = new EnrichInformation(
                "Database",
                "DB-001",
                "Connection failed",
                ErrorLevel.ERROR
        );

        information.addMetadata("host", "localhost");

        String result = formatter.format(List.of(information));

        assertTrue(result.contains("[host=localhost]"));
    }

    // ==================== Multiple Information ====================

    /**
     * Should format multiple information entries in their original order.
     */
    @Test
    void shouldPreserveInformationOrder() {
        ConsoleConfig config = new ConsoleConfig();
        DefaultEnrichFormatter formatter = new DefaultEnrichFormatter(config);

        EnrichInformation first = new EnrichInformation(
                "Database", "DB-001", "Database failed", ErrorLevel.ERROR
        );

        EnrichInformation second = new EnrichInformation(
                "Cache", "CACHE-001", "Cache failed", ErrorLevel.WARNING
        );

        String result = formatter.format(List.of(first, second));

        assertTrue(result.indexOf("Database") < result.indexOf("Cache"));
    }

    /**
     * Should number multiple errors sequentially.
     */
    @Test
    void shouldNumberMultipleErrorsSequentially() {
        ConsoleConfig config = new ConsoleConfig();
        DefaultEnrichFormatter formatter = new DefaultEnrichFormatter(config);

        EnrichInformation first = new EnrichInformation(
                "Database", "DB-001", "Database failed", ErrorLevel.ERROR
        );

        EnrichInformation second = new EnrichInformation(
                "Cache", "CACHE-001", "Cache failed", ErrorLevel.WARNING
        );

        String result = formatter.format(List.of(first, second));

        assertTrue(result.contains("[ERROR-1]"));
        assertTrue(result.contains("[ERROR-2]"));
    }

    /**
     * Should report the total number of formatted errors.
     */
    @Test
    void shouldReportTotalErrorCount() {
        ConsoleConfig config = new ConsoleConfig();
        DefaultEnrichFormatter formatter = new DefaultEnrichFormatter(config);

        EnrichInformation first = new EnrichInformation(
                "Database", "DB-001", "Database failed", ErrorLevel.ERROR
        );

        EnrichInformation second = new EnrichInformation(
                "Cache", "CACHE-001", "Cache failed", ErrorLevel.WARNING
        );

        String result = formatter.format(List.of(first, second));

        assertTrue(result.startsWith("[2-ERRORS]"));
    }

    /**
     * Should format an empty information list without throwing an exception.
     */
    @Test
    void shouldFormatEmptyInformationList() {
        ConsoleConfig config = new ConsoleConfig();
        DefaultEnrichFormatter formatter = new DefaultEnrichFormatter(config);

        String result = formatter.format(List.of());

        assertEquals("[0-ERRORS]\n", result);
    }
}