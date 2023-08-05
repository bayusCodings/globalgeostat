package com.klasha.globalgeostat.commons.geo.dto.population;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CityPopulationData {
    private String city;
    private String country;
    private List<PopulationCount> populationCounts;
}
