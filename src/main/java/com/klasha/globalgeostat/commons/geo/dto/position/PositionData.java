package com.klasha.globalgeostat.commons.geo.dto.position;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionData {
    private double lat;

    @JsonProperty("long")
    private double lng;
}
