package com.enrichable.exception;

import com.enrichable.exception.config.ErrorLevel;
import com.enrichable.exception.config.ExceptionConfiguration;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Regular exceptions: "I failed."
 * EnrichableException: "Allow me to explain exactly how."
 */
public class EnrichableException extends RuntimeException {
    private static ExceptionConfiguration config = new ExceptionConfiguration();
    public static void setConfig(ExceptionConfiguration configuration) {
        config = configuration;
    }
    private int exceptionCounter = 0;
    private static class ExceptionInformation {
        String exceptionContext;        // Where the damage happened
        String exceptionCode;           // The error's ID card
        String exceptionMessage;        // What actually went wrong
        String exceptionDateTime;       // When the chaos happened?
        ErrorLevel exceptionErrorLevel; // How doomed are we?
        ExceptionInformation(String exceptionContext,
                             String exceptionCode,
                             String exceptionMessage,
                             ErrorLevel exceptionErrorLevel) {
            this.exceptionContext = exceptionContext;
            this.exceptionCode = exceptionCode;
            this.exceptionMessage = exceptionMessage;
            this.exceptionDateTime = LocalDateTime
                    .now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.exceptionErrorLevel = exceptionErrorLevel;
        }
    }
    // The MC, who is gonna carry all the exceptions
    @SuppressWarnings("all")
    private List<ExceptionInformation> informationList = new ArrayList<>();
    public EnrichableException(String exceptionContext,
                               String exceptionCode,
                               String exceptionMessage,
                               ErrorLevel exceptionErrorLevel) {
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
        this.exceptionCounter++; // One more victim added to the list.
        informationList.add(new ExceptionInformation(
                exceptionContext,
                exceptionCode,
                exceptionMessage,
                exceptionErrorLevel
        ));
        return this; // Yes, we are chaining this thing.
    }
    @Override
    public String toString() { // This is the place were we unpack the trauma XD.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[")
                .append(exceptionCounter)
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
        }
        // The errors told their story. Now for the supporting evidence.
        if (config.getShowMetadata())
            for (Map.Entry<String, String> metadataInfo : metadataMap.entrySet()) {
                stringBuilder.append("[")
                        .append(metadataInfo.getKey())
                        .append("=")
                        .append(metadataInfo.getValue())
                        .append("]")
                        .append("\n");
            }
        return stringBuilder.toString();
    }
    public void log() {
        String result = this.toString();
        logToFile(result);
    }
    // Because apparently printing it isn't enough.
    private void logToFile(String content) {
        // Dear future me, good luck debugging this.
        try (FileWriter writer = new FileWriter("errors.log", true)) {
            writer.write(content);
            writer.write("\n---\n"); // who doesn't like a clear space?
        } catch (IOException e) {
            // Peak comedy.
            System.out.println("[The error logger failed while logging an error.] " + e.getMessage());
        }
    }
    private final Map<String, String> metadataMap = new HashMap<>();
    // Give the error some receipts.
    public EnrichableException addMetaData(String key, String value) {
        metadataMap.put(key, value);
        return this;
    }
}
