package com.klasha.globalgeostat.service.globalstat;

import com.klasha.globalgeostat.commons.Countries;
import com.klasha.globalgeostat.commons.conversion.ConversionService;
import com.klasha.globalgeostat.commons.geo.GeoService;
import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalData;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyData;
import com.klasha.globalgeostat.commons.geo.dto.population.CityPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;
import com.klasha.globalgeostat.commons.geo.dto.state.State;
import com.klasha.globalgeostat.commons.geo.dto.state.StateData;
import com.klasha.globalgeostat.core.exception.ConflictException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;
import com.klasha.globalgeostat.util.GlobalStatUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class GlobalStatServiceTest {
    private GlobalStatService service;
    private GeoService geoService;
    private ConversionService conversionService;

    @BeforeEach
    public void setUp() {
        geoService = mock(GeoService.class);
        conversionService = mock(ConversionService.class);
        service = new GlobalStatServiceImpl(geoService, conversionService);
    }

    @Test
    public void testGetMostPopulatedCities() throws Exception {
        // Mocked data
        List<CityPopulationData> italyData = List.of(
            GlobalStatUtil.toMockedCityPopulationData(
                Countries.ITALY, List.of(GlobalStatUtil.toMockedPopulationCount("2023", "1000000")))
        );

        List<CityPopulationData> newZealandData = List.of(
            GlobalStatUtil.toMockedCityPopulationData(
                Countries.NEW_ZEALAND, List.of(GlobalStatUtil.toMockedPopulationCount("2023", "1500000")))
        );

        List<CityPopulationData> ghanaData = List.of(
            GlobalStatUtil.toMockedCityPopulationData(
                Countries.GHANA, List.of(GlobalStatUtil.toMockedPopulationCount("2023", "800000")))
        );

        when(geoService.getCityPopulation(Countries.ITALY)).thenReturn(italyData);
        when(geoService.getCityPopulation(Countries.NEW_ZEALAND)).thenReturn(newZealandData);
        when(geoService.getCityPopulation(Countries.GHANA)).thenReturn(ghanaData);

        // Test the service method
        List<String> result = service.getMostPopulatedCities(2);

        // Verify that the geoService.getCityPopulation was called for each country
        verify(geoService, times(3)).getCityPopulation(anyString());

        // Verify the correctness of the result
        assertEquals(List.of(Countries.NEW_ZEALAND, Countries.ITALY), result);
    }

    @Test
    public void testGetCountryInfo() throws Exception {
        String country = "Nigeria";
        String capital = "Abuja";

        // Mocked data
        CountryPopulationData countryPopulationData = GlobalStatUtil.toMockedCountryPopulationData(
            country, List.of(GlobalStatUtil.toMockedPopulationCount("2023", "1000000")));
        CapitalData capitalData = GlobalStatUtil.toMockedCapitalData(capital);
        PositionData positionData = GlobalStatUtil.toMockedPositionData(41.9028, 12.4964);
        CurrencyData currencyData = GlobalStatUtil.toMockedCurrencyData("NGN", "NG", "NGA");

        when(geoService.getCountryPopulation(country)).thenReturn(countryPopulationData);
        when(geoService.getCapital(country)).thenReturn(capitalData);
        when(geoService.getLocation(country)).thenReturn(positionData);
        when(geoService.getCurrency(country)).thenReturn(currencyData);

        // Test the method
        CountryInfo result = service.getCountryInfo(country);

        // Verify that the relevant methods were called
        verify(geoService).getCountryPopulation(country);
        verify(geoService).getCapital(country);
        verify(geoService).getLocation(country);
        verify(geoService).getCurrency(country);

        // Verify the correctness of the result
        assertEquals(capital, result.getCapitalCity());
        assertEquals(positionData, result.getLocation());
    }
    
    @Test
    public void testCurrencyConversionSuccess() throws Exception {
        String country = "Nigeria";
        String currency = "NGN";
        String targetCurrency = "EUR";
        BigDecimal amount = new BigDecimal(100);
        double conversionRate = 10.0;

        // Mocking the necessary behavior
        when(geoService.getCurrency(anyString())).thenReturn(GlobalStatUtil.toMockedCurrencyData(currency));
        when(conversionService.getConversionRate(anyString(), anyString())).thenReturn(conversionRate);

        // Call the method being tested
        ConversionInfo result = service.currencyConversion(country, amount, targetCurrency);
        BigDecimal expectedAmount = amount.multiply(new BigDecimal(conversionRate))
            .setScale(2, RoundingMode.HALF_UP);

        // Assertion
        assertEquals(currency, result.getCountryCurrency());
        assertEquals(expectedAmount, result.getConvertedAmount());
    }

    @Test
    public void testCurrencyConversionConflictException() throws Exception {
        String country = "Nigeria";
        String currency = "NGN";
        String targetCurrency = "NGN";
        BigDecimal amount = new BigDecimal(100);

        when(geoService.getCurrency(anyString())).thenReturn(GlobalStatUtil.toMockedCurrencyData(currency));

        // Call the method being tested with same source and target currency
        assertThrows(ConflictException.class, () -> {
            service.currencyConversion(country, amount, targetCurrency);
        });
    }

    @Test
    public void testCurrencyConversionResourceNotFoundException() throws Exception {
        String country = "Nigeria";
        String currency = "NGN";
        String targetCurrency = "EUR";
        BigDecimal amount = new BigDecimal(100);

        when(geoService.getCurrency(anyString())).thenReturn(GlobalStatUtil.toMockedCurrencyData(currency));
        when(conversionService.getConversionRate(anyString(), anyString())).thenReturn(null);

        // Call the method being tested with same source and target currency
        assertThrows(ResourceNotFoundException.class, () -> {
            service.currencyConversion(country, amount, targetCurrency);
        });
    }

    @Test
    public void testGetStateInfo() throws Exception {
        String country = "Nigeria";

        List<State> states = List.of(
            GlobalStatUtil.toMockedState("Lagos", "LG"),
            GlobalStatUtil.toMockedState("Oyo", "OY")
        );

        StateData stateData = GlobalStatUtil.toMockedStateData(states);

        when(geoService.getStates(country)).thenReturn(stateData);
        when(geoService.getCities(country, "Lagos")).thenReturn(List.of("Berger", "Ikeja"));
        when(geoService.getCities(country, "Oyo")).thenReturn(List.of("Ibadan", "Challenge"));

        // Test the method
        List<StateInfo> result = service.getStateInfo(country);

        // Verify that the relevant methods were called
        verify(geoService).getStates(country);
        verify(geoService).getCities(country, "Lagos");
        verify(geoService).getCities(country, "Oyo");

        // Verify the correctness of the result
        assertEquals(2, result.size());
        assertEquals("Lagos", result.get(0).getName());
        assertEquals("LG", result.get(0).getStateCode());
        assertEquals(2, result.get(0).getCities().size());
    }
}
