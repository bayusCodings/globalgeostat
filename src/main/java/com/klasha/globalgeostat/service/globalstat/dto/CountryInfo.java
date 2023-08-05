package com.klasha.globalgeostat.service.globalstat.dto;

import com.klasha.globalgeostat.commons.geo.dto.population.PopulationCount;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryInfo {
    private List<PopulationCount> population;
    private PositionData location;
    private String capitalCity;
    private String currency;
    private String iso2;
    private String iso3;
}
