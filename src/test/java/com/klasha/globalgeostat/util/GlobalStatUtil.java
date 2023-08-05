package com.klasha.globalgeostat.util;

import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalData;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyData;
import com.klasha.globalgeostat.commons.geo.dto.population.CityPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.PopulationCount;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;

import java.util.List;

public class GlobalStatUtil {
    public static CurrencyData toMockedCurrencyData(String currency) {
        return CurrencyData.builder()
            .currency(currency)
            .build();
    }

    public static CurrencyData toMockedCurrencyData(String currency, String iso2, String iso3) {
        return CurrencyData.builder()
            .currency(currency)
            .iso2(iso2)
            .iso3(iso3)
            .build();
    }

    public static PopulationCount toMockedPopulationCount(String year, String value) {
        return PopulationCount.builder()
            .year(year)
            .value(value)
            .build();
    }

    public static CityPopulationData toMockedCityPopulationData(String city, List<PopulationCount> populationCounts) {
        return CityPopulationData.builder()
            .city(city)
            .populationCounts(populationCounts)
            .build();
    }

    public static CountryPopulationData toMockedCountryPopulationData(String country, List<PopulationCount> populationCounts) {
        return CountryPopulationData.builder()
            .country(country)
            .populationCounts(populationCounts)
            .build();
    }

    public static CapitalData toMockedCapitalData(String capital) {
        return CapitalData.builder()
            .capital(capital)
            .build();
    }

    public static PositionData toMockedPositionData(double lat, double lng) {
        return PositionData.builder()
            .lat(lat)
            .lng(lng)
            .build();
    }
}
