package com.klasha.globalgeostat.service.globalstat.mapper;

import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalData;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;

import java.math.BigDecimal;
import java.util.List;

public class StatMapper {
    public static CountryInfo mapToCountryInfo(
            CountryPopulationData countryData, CapitalData capitalData, PositionData positionData, CurrencyData currencyData) {
        return CountryInfo.builder()
            .population(countryData.getPopulationCounts())
            .location(positionData)
            .capitalCity(capitalData.getCapital())
            .currency(currencyData.getCurrency())
            .iso2(currencyData.getIso2())
            .iso3(currencyData.getIso3())
            .build();
    }

    public static StateInfo mapToStateInfo(String name, String stateCode, List<String> cities) {
        return StateInfo.builder()
            .name(name)
            .stateCode(stateCode)
            .cities(cities)
            .build();
    }
    public static ConversionInfo mapToConversionInfo(String countryCurrency, BigDecimal convertedAmount) {
        return ConversionInfo.builder()
            .countryCurrency(countryCurrency)
            .convertedAmount(convertedAmount)
            .build();
    }
}
