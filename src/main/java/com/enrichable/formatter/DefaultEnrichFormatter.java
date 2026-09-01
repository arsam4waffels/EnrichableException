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
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(list.size()).append("-ERRORS]");
        for (EnrichInformation info : list) {
            sb.append("[").append(info.getContext())
                    .append(":").append(info.getCode()).append("]");
        }
        sb.append("\n");

        for (int i = 0; i < list.size(); i++) {
            EnrichInformation info = list.get(i);

            if (config.isShowErrorCount())
                sb.append("[ERROR-").append(i + 1).append("]");

            if (config.isShowTimestamp())
                sb.append("[")
                        .append(info.getDateTime().format(DATE_TIME_FORMATTER))
                        .append("]");

            if (config.isShowErrorLevel())
                sb.append("[").append(info.getErrorLevel()).append("]");

            sb.append("[")
                    .append(info.getContext())
                    .append(":")
                    .append(info.getCode())
                    .append("] ")
                    .append(info.getMessage())
                    .append("\n");

            if (config.isShowMetadata())
                for (Map.Entry<String, String> e : info.getMetadata().entrySet())
                    sb.append("[")
                            .append(e.getKey())
                            .append("=")
                            .append(e.getValue())
                            .append("]\n");
        }
        return sb.toString();
    }
}