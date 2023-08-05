package com.klasha.globalgeostat.commons.geo.dto.population;

import lombok.Data;

@Data
public class CountryPopulationResponse {
    private boolean error;
    private String msg;
    private CountryPopulationData data;
}
