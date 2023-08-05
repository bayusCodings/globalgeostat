package com.klasha.globalgeostat.commons.geo.dto.currency;

import lombok.Data;

@Data
public class CurrencyResponse {
    private boolean error;
    private String msg;
    private CurrencyData data;
}
