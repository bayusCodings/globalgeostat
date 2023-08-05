package com.klasha.globalgeostat.commons.geo.dto.state;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StateData {
    private List<State> states;
}
