package com.klasha.globalgeostat.commons.geo.dto.state;

import lombok.Data;

@Data
public class StateResponse {
    private boolean error;
    private String msg;
    private StateData data;
}
