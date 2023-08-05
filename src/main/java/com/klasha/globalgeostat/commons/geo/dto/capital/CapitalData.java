package com.klasha.globalgeostat.commons.geo.dto.capital;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CapitalData {
    private String name;
    private String capital;
    private String iso2;
    private String iso3;
}
