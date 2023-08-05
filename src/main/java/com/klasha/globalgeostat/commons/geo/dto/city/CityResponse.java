package com.klasha.globalgeostat.commons.geo.dto.city;

import lombok.Data;

import java.util.List;

@Data
public class CityResponse {
    private boolean error;
    private String msg;
    private List<String> data;
}
