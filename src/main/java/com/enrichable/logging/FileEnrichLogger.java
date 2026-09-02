package com.enrichable.logging;

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
    private static final String LOG_FILE = "enrichable.log";
    private FileEnrichLogger() {}
    public void write(List<EnrichInformation> informationList, String thrownAt) {
        synchronized (LOG_LOCK) {
            String report = buildReport(informationList, thrownAt);
            writeToFile(report);
        }
    }
    public static FileEnrichLogger getInstance() {
        return INSTANCE;
    }
    private String buildReport(List<EnrichInformation> list, String thrownAt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("════════════════════════════════════════════════════\n");
        stringBuilder.append("  ENRICHABLE EXCEPTION REPORT\n");
        stringBuilder.append("  Total Errors : ").append(list.size()).append("\n");
        stringBuilder.append("  Thrown At    : ").append(thrownAt).append("\n");
        stringBuilder.append("════════════════════════════════════════════════════\n");
        for (int i = 0; i < list.size(); i++) {
            EnrichInformation info = list.get(i);
            stringBuilder.append("\n  [ERROR-")
                    .append(i + 1)
                    .append("] [")
                    .append(info.getErrorLevel())
                    .append("] [")
                    .append(info.getContext())
                    .append(":")
                    .append(info.getCode())
                    .append("]\n");
            stringBuilder.append("  ")
                    .append(info.getMessage())
                    .append("\n");
            stringBuilder.append("    └─ Time : ")
                    .append(info.getDateTime())
                    .append("\n");
            for (Map.Entry<String, String> stringEntry : info.getMetadata().entrySet())
                stringBuilder.append("    └─ ")
                        .append(stringEntry.getKey())
                        .append(" : ")
                        .append(stringEntry.getValue())
                        .append("\n");
        }
        return stringBuilder.toString();
    }
    private void writeToFile(String content) {
        try (var writer = Files.newBufferedWriter(
                Path.of(LOG_FILE),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("[The error logger failed while logging an error.] "
                    + e.getMessage());
        }
    }
}
