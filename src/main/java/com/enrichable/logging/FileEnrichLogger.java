package com.enrichable.logging;

import com.enrichable.config.ErrorLevel;
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
                    .filter(info -> info.getErrorLevel() == config.onlyLevel())
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("════════════════════════════════════════════════════\n");
        stringBuilder.append("  ENRICHABLE EXCEPTION REPORT\n");
        stringBuilder.append("  Total Errors : ")
                .append(list.size())
                .append("\n");
        if (config.showTimestamp()) {
            stringBuilder.append("  Thrown At    : ")
                    .append(thrownAt)
                    .append("\n");
        }
        stringBuilder.append("════════════════════════════════════════════════════\n");
        for (int i = 0; i < list.size(); i++) {
            EnrichInformation info = list.get(i);
            stringBuilder.append("\n  [ERROR-")
                    .append(i + 1)
                    .append("] ");
            if (config.showErrorLevel())
                stringBuilder.append("[")
                        .append(info.getErrorLevel())
                        .append("] ");
            stringBuilder.append("[")
                    .append(info.getContext())
                    .append(":")
                    .append(info.getCode())
                    .append("]\n");
            stringBuilder.append("  ")
                    .append(info.getMessage())
                    .append("\n");
            if (config.showTimestamp())
                stringBuilder.append("    └─ Time : ")
                        .append(info.getDateTime())
                        .append("\n");
            if (config.showMetadata()) {
                for (Map.Entry<String, String> entry :
                        info.getMetadata().entrySet()) {
                    stringBuilder.append("    └─ ")
                            .append(entry.getKey())
                            .append(" : ")
                            .append(entry.getValue())
                            .append("\n");
                }
            }
        }
        return stringBuilder.toString();
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