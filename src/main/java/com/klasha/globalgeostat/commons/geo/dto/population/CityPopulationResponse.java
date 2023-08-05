package com.klasha.globalgeostat.commons.geo.dto.population;

import lombok.Data;

import java.util.List;

@Data
public class CityPopulationResponse {
    private boolean error;
    private String msg;
    private List<CityPopulationData> data;
}
