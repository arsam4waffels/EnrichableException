package com.enrichable.formatter;

import java.time.format.DateTimeFormatter;
import com.enrichable.config.ConsoleConfig;
import com.enrichable.model.EnrichInformation;
import java.util.List;
import java.util.Map;

public class DefaultEnrichFormatter implements EnrichFormatter {
    private final ConsoleConfig consoleConfig;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public DefaultEnrichFormatter(ConsoleConfig consoleConfig) {
        this.consoleConfig = consoleConfig;
    }
    @Override
    public String format(List<EnrichInformation> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(list.size()).append("-ERRORS]");
        for (EnrichInformation info : list) {
            stringBuilder.append("[").append(info.getContext())
                    .append(":").append(info.getCode()).append("]");
        }
        stringBuilder.append("\n");
        for (int i = 0; i < list.size(); i++) {
            EnrichInformation info = list.get(i);

            if (consoleConfig.showErrorCount())
                stringBuilder.append("[ERROR-")
                        .append(i + 1)
                        .append("]");

            if (consoleConfig.showTimestamp())
                stringBuilder.append("[")
                        .append(info.getDateTime().format(DATE_TIME_FORMATTER))
                        .append("]");

            if (consoleConfig.showErrorLevel())
                stringBuilder.append("[")
                        .append(info.getErrorLevel())
                        .append("]");

            stringBuilder.append("[")
                    .append(info.getContext())
                    .append(":")
                    .append(info.getCode())
                    .append("] ")
                    .append(info.getMessage())
                    .append("\n");

            if (consoleConfig.showMetadata())
                for (Map.Entry<String, String> stringEntry : info.getMetadata().entrySet())
                    stringBuilder.append("[")
                            .append(stringEntry.getKey())
                            .append("=")
                            .append(stringEntry.getValue())
                            .append("]\n");
        }
        return stringBuilder.toString();
    }
}