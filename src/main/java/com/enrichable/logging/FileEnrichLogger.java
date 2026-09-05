package com.enrichable.logging;

import com.enrichable.config.LogConfig;
import com.enrichable.model.EnrichInformation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class FileEnrichLogger {

    private static final Object LOG_LOCK = new Object();
    private static final FileEnrichLogger INSTANCE = new FileEnrichLogger();

    private FileEnrichLogger() {}

    public void write(
            List<EnrichInformation> informationList,
            String thrownAt,
            LogConfig config) {

        synchronized (LOG_LOCK) {
            List<EnrichInformation> loggable = filter(
                    informationList,
                    config
            );

            String report = buildReport(loggable, thrownAt, config);

            writeToFile(report, config);
        }
    }

    public static FileEnrichLogger getInstance() {
        return INSTANCE;
    }

    private List<EnrichInformation> filter(
            List<EnrichInformation> informationList,
            LogConfig config) {

        if (config.onlyLevel() != null)
            return informationList.stream()
                    .filter(info ->
                            info.getErrorLevel() == config.onlyLevel())
                    .toList();

        if (config.minimumLevel() != null)
            return informationList.stream()
                    .filter(info ->
                            info.getErrorLevel().ordinal()
                                    >= config.minimumLevel().ordinal())
                    .toList();

        return informationList;
    }

    private String buildReport(
            List<EnrichInformation> list,
            String thrownAt,
            LogConfig config) {

        StringBuilder report = new StringBuilder();

        appendHeader(report, list.size(), thrownAt, config);

        for (int i = 0; i < list.size(); i++) {
            appendError(report, list.get(i), i, config);
        }

        return report.toString();
    }

    private void appendHeader(
            StringBuilder report,
            int errorCount,
            String thrownAt,
            LogConfig config) {

        report.append("════════════════════════════════════════════════════\n");
        report.append("  ENRICHABLE EXCEPTION REPORT\n");
        report.append("  Total Errors : ")
                .append(errorCount)
                .append("\n");

        if (config.showTimestamp()) {
            report.append("  Thrown At    : ")
                    .append(thrownAt)
                    .append("\n");
        }

        report.append("════════════════════════════════════════════════════\n");
    }

    private void appendError(
            StringBuilder report,
            EnrichInformation info,
            int index,
            LogConfig config) {

        report.append("\n  [ERROR-")
                .append(index + 1)
                .append("] ");

        if (config.showErrorLevel()) {
            report.append("[")
                    .append(info.getErrorLevel())
                    .append("] ");
        }

        report.append("[")
                .append(info.getContext())
                .append(":")
                .append(info.getCode())
                .append("]\n");

        report.append("  ")
                .append(info.getMessage())
                .append("\n");

        if (config.showTimestamp()) {
            report.append("    └─ Time : ")
                    .append(info.getDateTime())
                    .append("\n");
        }

        if (config.showMetadata()) {
            appendMetadata(report, info);
        }
    }

    private void appendMetadata(
            StringBuilder report,
            EnrichInformation info) {

        for (Map.Entry<String, String> entry :
                info.getMetadata().entrySet()) {

            report.append("    └─ ")
                    .append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append("\n");
        }
    }

    private void writeToFile(String content, LogConfig config) {
        try {
            Path path = Path.of(config.filePath());
            if (config.clearBeforeWrite()) {
                Files.writeString(
                        path,
                        content,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                return;
            }
            Files.writeString(
                    path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println(
                    "[The error logger failed while logging an error.] "
                            + e.getMessage());
        }
    }
}