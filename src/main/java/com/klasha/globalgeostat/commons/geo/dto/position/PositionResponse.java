package com.klasha.globalgeostat.commons.geo.dto.position;

import lombok.Data;

@Data
public class PositionResponse {
    private boolean error;
    private String msg;
    private PositionData data;
}
