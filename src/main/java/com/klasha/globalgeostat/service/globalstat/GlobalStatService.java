package com.klasha.globalgeostat.service.globalstat;

import com.klasha.globalgeostat.core.exception.ConflictException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;

import java.math.BigDecimal;
import java.util.List;

public interface GlobalStatService {
    List<String> getMostPopulatedCities(int N);
    CountryInfo getCountryInfo(String country);
    List<StateInfo> getStateInfo(String country);
    ConversionInfo currencyConversion(String country, BigDecimal amount, String targetCurrency)
        throws ResourceNotFoundException, ConflictException;
}
