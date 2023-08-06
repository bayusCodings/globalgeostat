package com.klasha.globalgeostat.controller;

import com.klasha.globalgeostat.commons.geo.dto.population.PopulationCount;
import com.klasha.globalgeostat.service.globalstat.GlobalStatService;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;
import com.klasha.globalgeostat.util.GlobalStatUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = GlobalStatController.class)
public class GlobalStatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GlobalStatService globalStatService;

    @Test
    void testGetMostPopulatedCities() throws Exception {
        List<String> mockedCities = List.of("City1", "City2", "City3");

        when(globalStatService.getMostPopulatedCities(10)).thenReturn(mockedCities);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/most-populated-cities")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0]").value("City1"))
            .andExpect(jsonPath("$.data[1]").value("City2"))
            .andExpect(jsonPath("$.data[2]").value("City3"));
    }

    @Test
    public void testGetCountryInfo() throws Exception {
        List<PopulationCount> population = List.of(GlobalStatUtil.toMockedPopulationCount("2023", "1000000"));
        CountryInfo countryInfo = CountryInfo.builder()
            .population(population).currency("NGN").capitalCity("Abuja").iso2("NG").iso3("NGA").build();
        when(globalStatService.getCountryInfo("Nigeria")).thenReturn(countryInfo);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/country-info/{country}", "Nigeria")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.population").isArray())
            .andExpect(jsonPath("$.data.population[0].value").value("1000000"))
            .andExpect(jsonPath("$.data.currency").value("NGN"))
            .andExpect(jsonPath("$.data.capitalCity").value("Abuja"))
            .andExpect(jsonPath("$.data.iso2").value("NG"))
            .andExpect(jsonPath("$.data.iso3").value("NGA"));
    }

    @Test
    public void testGetStateInfo() throws Exception {
        StateInfo stateInfo = StateInfo.builder()
            .name("Lagos").stateCode("LG").cities(List.of("Berger", "Ikeja")).build();
        List<StateInfo> stateInfoList = List.of(stateInfo);

        when(globalStatService.getStateInfo("Nigeria")).thenReturn(stateInfoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/state-info/{country}", "Nigeria")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].name").value("Lagos"))
            .andExpect(jsonPath("$.data[0].stateCode").value("LG"))
            .andExpect(jsonPath("$.data[0].cities").isArray())
            .andExpect(jsonPath("$.data[0].cities[0]").value("Berger"));
    }

    @Test
    public void testCurrencyConversion() throws Exception {
        BigDecimal amount = new BigDecimal("920.00");
        ConversionInfo conversionInfo = ConversionInfo.builder()
            .convertedAmount(new BigDecimal("1.00")).countryCurrency("NGN").build();
        when(globalStatService.currencyConversion("Nigeria", amount, "EUR")).thenReturn(conversionInfo);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/convert/{country}?amount=920.00&targetCurrency=EUR", "Nigeria")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.countryCurrency").value("NGN"))
            .andExpect(jsonPath("$.data.convertedAmount").value(1.00));
    }
}
