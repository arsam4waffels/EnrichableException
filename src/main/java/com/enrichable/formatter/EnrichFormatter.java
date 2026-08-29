package com.enrichable.formatter;

import com.enrichable.model.EnrichInformation;
import java.util.List;

public interface EnrichFormatter {
    String format(List<EnrichInformation> informationList);
}