package com.klasha.globalgeostat.commons.geo.dto.population;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopulationCount {
    private String year;
    private String value;
}
