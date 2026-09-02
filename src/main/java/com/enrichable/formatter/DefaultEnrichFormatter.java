package com.enrichable.formatter;

import java.time.format.DateTimeFormatter;
import com.enrichable.config.EnrichConfiguration;
import com.enrichable.model.EnrichInformation;
import java.util.List;
import java.util.Map;

public class DefaultEnrichFormatter implements EnrichFormatter {
    private final EnrichConfiguration config;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public DefaultEnrichFormatter(EnrichConfiguration config) {
        this.config = config;
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

            if (config.isShowErrorCount())
                stringBuilder.append("[ERROR-")
                        .append(i + 1)
                        .append("]");

            if (config.isShowTimestamp())
                stringBuilder.append("[")
                        .append(info.getDateTime().format(DATE_TIME_FORMATTER))
                        .append("]");

            if (config.isShowErrorLevel())
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

            if (config.isShowMetadata())
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