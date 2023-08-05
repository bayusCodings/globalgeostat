package com.klasha.globalgeostat.commons.geo.dto.population;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryPopulationData {
    private String country;
    private String code;
    private List<PopulationCount> populationCounts;
}
