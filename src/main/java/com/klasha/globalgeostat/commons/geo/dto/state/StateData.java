package com.klasha.globalgeostat.commons.geo.dto.state;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StateData {
    private String name;
    private String iso2;
    private String iso3;
    private List<State> states;
}
