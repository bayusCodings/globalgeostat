package com.klasha.globalgeostat.commons.geo.dto.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyData {
    private String name;
    private String currency;
    private String iso2;
    private String iso3;
}
