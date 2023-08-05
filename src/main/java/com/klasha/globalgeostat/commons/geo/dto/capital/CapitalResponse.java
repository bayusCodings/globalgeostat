package com.klasha.globalgeostat.commons.geo.dto.capital;

import lombok.Data;

@Data
public class CapitalResponse {
    private boolean error;
    private String msg;
    private CapitalData data;
}
