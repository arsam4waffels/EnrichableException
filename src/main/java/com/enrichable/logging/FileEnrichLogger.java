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
    private static final String LOG_FILE = "enrichable.log";
    public void write(List<EnrichInformation> list, String thrownAt) {
        String report = buildReport(list, thrownAt);
        writeToFile(report);
    }
    private String buildReport(List<EnrichInformation> list, String thrownAt) {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════════════════\n");
        sb.append("  ENRICHABLE EXCEPTION REPORT\n");
        sb.append("  Total Errors : ").append(list.size()).append("\n");
        sb.append("  Thrown At    : ").append(thrownAt).append("\n");
        sb.append("════════════════════════════════════════════════════\n");
        for (int i = 0; i < list.size(); i++) {
            EnrichInformation info = list.get(i);
            sb.append("\n  [ERROR-")
                    .append(i + 1)
                    .append("] [")
                    .append(info.getErrorLevel())
                    .append("] [")
                    .append(info.getContext())
                    .append(":")
                    .append(info.getCode())
                    .append("]\n");
            sb.append("  ")
                    .append(info.getMessage())
                    .append("\n");
            sb.append("    └─ Time : ")
                    .append(info.getDateTime())
                    .append("\n");
            for (Map.Entry<String, String> e : info.getMetadata().entrySet())
                sb.append("    └─ ")
                        .append(e.getKey())
                        .append(" : ")
                        .append(e.getValue())
                        .append("\n");
        }
        return sb.toString();
    }
    private void writeToFile(String content) {
        synchronized (LOG_LOCK) {
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
}
