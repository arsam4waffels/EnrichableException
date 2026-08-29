package com.enrichable;

import com.enrichable.config.ErrorLevel;
import com.enrichable.config.ExceptionConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Regular exceptions: "I failed."
 * EnrichableException: "Allow me to explain exactly how."
 */
public class EnrichableException extends RuntimeException {
    private ExceptionConfiguration config = new ExceptionConfiguration();
    public void setConfig(ExceptionConfiguration configuration) {
        if (configuration == null) throw new IllegalArgumentException(
                "Exception configuration cannot be null."
        );
        this.config = configuration;
    }
    private static final Object LOG_LOCK = new Object();
    // Time of death, formatted for the record.
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter  DATE_TIME_FORMATER =
            DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final class ExceptionInformation {
        final String exceptionContext;        // Where the damage happened
        final String exceptionCode;           // The error's ID card
        final String exceptionMessage;        // What actually went wrong
        final String exceptionDateTime;       // When the chaos happened?
        final ErrorLevel exceptionErrorLevel; // How doomed are we?

        private final Map<String, String> metadataMap = new HashMap<>();

        ExceptionInformation(String exceptionContext,
                             String exceptionCode,
                             String exceptionMessage,
                             ErrorLevel exceptionErrorLevel) {
            this.exceptionContext = exceptionContext;
            this.exceptionCode = exceptionCode;
            this.exceptionMessage = exceptionMessage;
            this.exceptionDateTime = LocalDateTime
                    .now()
                    .format(DATE_TIME_FORMATER);
            this.exceptionErrorLevel = exceptionErrorLevel;
        }
    }
    // Wall of shame. Every entry earned its place.
    private final List<ExceptionInformation> informationList = new ArrayList<>();
    public EnrichableException(String exceptionContext,
                               String exceptionCode,
                               String exceptionMessage,
                               ErrorLevel exceptionErrorLevel,
                               Throwable exceptionCause) {
        // I'm just letting Java remember who started this whole mess.
        super(exceptionMessage, exceptionCause);
        addInformation(
                exceptionContext,
                exceptionCode,
                exceptionMessage,
                exceptionErrorLevel
        );
    }
    public EnrichableException addInformation(String exceptionContext,
                                              String exceptionCode,
                                              String exceptionMessage,
                                              ErrorLevel exceptionErrorLevel) {
        // Before we add another victim, make sure it's a valid one. Otherwise, explode.
        validateRequiredText(exceptionContext, "Exception context");
        validateRequiredText(exceptionCode, "Exception code");
        validateRequiredText(exceptionMessage, "Exception message");
        validateErrorLevel(exceptionErrorLevel);

        informationList.add(new ExceptionInformation(
                exceptionContext,
                exceptionCode,
                exceptionMessage,
                exceptionErrorLevel
        ));
        return this; // One error at a time, one chain at a time.
    }
    private void validateRequiredText(String value, String fieldName) {
        // Null is not a personality trait. Fix it before proceeding.
        if (value == null)
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        // An empty error description is not exactly helpful. why are you even doing this?
        if (value.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
    }
    private void validateErrorLevel(ErrorLevel errorLevel) {
        // An error without a severity level? Bold strategy mate.
        if (errorLevel == null)
            throw new IllegalArgumentException("Error level cannot be null.");
    }

    @Override
    public String toString() { // This is the place were we unpack the trauma XD.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[")
                .append(informationList.size())
                .append("-ERRORS")
                .append("]");
        for (ExceptionInformation exceptionInformation : informationList) {
            stringBuilder.append("[")
                    .append(exceptionInformation.exceptionContext)
                    .append(":")
                    .append(exceptionInformation.exceptionCode)
                    .append("]");
        }
        stringBuilder.append("\n");
        // Time to turn suffering into text
        for (int i = 0; i < informationList.size(); i++) {
            ExceptionInformation exceptionInformation = informationList.get(i);

            if (config.getShowErrorCount())
                stringBuilder.append("[ERROR-").append(i + 1).append("]");

            if (config.getShowTimestamp())
                stringBuilder.append("[").append(exceptionInformation.exceptionDateTime).append("]");

            if (config.getShowErrorLevel())
                stringBuilder.append("[").append(exceptionInformation.exceptionErrorLevel).append("]");

            stringBuilder.append("[")
                    .append(exceptionInformation.exceptionContext)
                    .append(":")
                    .append(exceptionInformation.exceptionCode)
                    .append("] ")
                    .append(exceptionInformation.exceptionMessage)
                    .append("\n");
            // The errors told their story. Now for the supporting evidence.
            if (config.getShowMetadata())
                for (Map.Entry<String, String> metadataInfo : exceptionInformation.metadataMap.entrySet()) {
                    stringBuilder.append("[")
                            .append(metadataInfo.getKey())
                            .append("=")
                            .append(metadataInfo.getValue())
                            .append("]")
                            .append("\n");
                }
        }
        return stringBuilder.toString();
    }
    private final String exceptionDateTime = LocalDateTime
            .now()
            .format(DATE_TIME_FORMATER);
    private String buildLogReport() {
        StringBuilder builder = new StringBuilder();
        List<ExceptionInformation> loggableInformation = getLoggableInformation();
        builder.append("════════════════════════════════════════════════════\n");
        builder.append("  ENRICHABLE EXCEPTION REPORT\n");
        builder.append("  Total Errors : ")
                .append(loggableInformation.size())
                .append("\n");
        builder.append("  Thrown At    : ")
                .append(exceptionDateTime)
                .append("\n");
        builder.append("════════════════════════════════════════════════════\n");
        for (int i = 0; i < loggableInformation.size(); i++) {
            ExceptionInformation exceptionInformation = loggableInformation.get(i);

            builder.append("\n");
            builder.append("  [ERROR-")
                    .append(i + 1)
                    .append("] [")
                    .append(exceptionInformation.exceptionErrorLevel)
                    .append("] [")
                    .append(exceptionInformation.exceptionContext)
                    .append(":")
                    .append(exceptionInformation.exceptionCode)
                    .append("]\n");
            builder.append("  ")
                    .append(exceptionInformation.exceptionMessage)
                    .append("\n");
            builder.append("    └─ Time : ")
                    .append(exceptionInformation.exceptionDateTime)
                    .append("\n");
            for (Map.Entry<String, String> metadata : exceptionInformation.metadataMap.entrySet()) {
                builder.append("    └─ ")
                        .append(metadata.getKey())
                        .append(" : ")
                        .append(metadata.getValue())
                        .append("\n");
            }
        }
        return builder.toString();
    }
    public void writeLog() {
        String result = buildLogReport();
        logToFile(result);
    }
    // Because apparently printing it isn't enough.
    private void logToFile(String content) {
        synchronized (LOG_LOCK) {
            try (var writer = Files.newBufferedWriter(
                    Path.of("enrichable.log"),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                writer.write(content);
            } catch (IOException e) {
                // Peak comedy
                System.err.println(
                        "[The error logger failed while logging an error.] "
                                + e.getMessage()
                );
            }
        }
    }
    private ErrorLevel logErrorLevelFilter;
    // Because sometimes you only want the important stuff.
    public EnrichableException onlyLog(ErrorLevel errorLevel) {
        validateErrorLevel(errorLevel);
        this.logErrorLevelFilter = errorLevel;
        return this;
    }
    // Keep the exception intact. We're filtering the log, not rewriting history.
    private List<ExceptionInformation> getLoggableInformation() {
        if (logErrorLevelFilter == null)
            return informationList;
        // Only the chosen ones make it to the log.
        return informationList.stream()
                .filter(info -> info.exceptionErrorLevel == logErrorLevelFilter)
                .toList();
    }
    // Give the error some receipts.
    public EnrichableException addMetaData(String key, String value) {
        if (informationList.isEmpty())
            throw new IllegalStateException(
                    "Cannot add metadata without exception information."
            );
        ExceptionInformation currentInformation = informationList.getLast();
        currentInformation.metadataMap.put(
                normalizeMetadataKey(key),
                normalizeMetadataValue(value)
        );
        return this;
    }
    private String normalizeMetadataKey(String key) {
        // Metadata without a key? We're not doing anonymous paperwork.
        if (key == null) throw new IllegalArgumentException("Metadata key cannot be null.");
        // Empty is allowed. Unnamed emptiness is not.
        if (key.isBlank()) return "BLANK";
        return key;
    }
    private String normalizeMetadataValue(String value) {
        if (value == null) throw new IllegalArgumentException("Metadata value cannot be null.");
        if (value.isBlank()) return "BLANK";
        return value;
    }
}
