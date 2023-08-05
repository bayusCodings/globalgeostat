package com.klasha.globalgeostat.commons.geo.dto.state;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class State {
    private String name;

    @JsonProperty("state_code")
    private String stateCode;
}
