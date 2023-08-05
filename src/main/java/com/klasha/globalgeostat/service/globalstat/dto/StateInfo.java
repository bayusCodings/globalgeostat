package com.klasha.globalgeostat.service.globalstat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StateInfo {
    private String name;
    private String stateCode;
    private List<String> cities;
}
