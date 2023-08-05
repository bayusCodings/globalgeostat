package com.klasha.globalgeostat.controller;

import com.klasha.globalgeostat.core.base.Response;
import com.klasha.globalgeostat.core.exception.ConflictException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.klasha.globalgeostat.service.globalstat.GlobalStatService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class GlobalStatController {
    private final GlobalStatService globalStatService;

    @GetMapping(path = "/most-populated-cities")
    public ResponseEntity<Response<List<String>>> getMostPopulatedCities(
        @RequestParam(required = false, defaultValue = "10") int N
    ) {
        List<String> response = globalStatService.getMostPopulatedCities(N);
        return new ResponseEntity<>(new Response<>(response), HttpStatus.OK);
    }

    @GetMapping(path = "/country-info/{country}")
    public ResponseEntity<Response<CountryInfo>> getCountryInfo(@PathVariable String country) {
        CountryInfo countryInfo = globalStatService.getCountryInfo(country);
        return new ResponseEntity<>(new Response<>(countryInfo), HttpStatus.OK);
    }

    @GetMapping(path = "/state-info/{country}")
    public ResponseEntity<Response<List<StateInfo>>> getStateInfo(@PathVariable String country) {
        List<StateInfo> stateInfo = globalStatService.getStateInfo(country);
        return new ResponseEntity<>(new Response<>(stateInfo), HttpStatus.OK);
    }

    @GetMapping(path = "/convert/{country}")
    public ResponseEntity<Response<ConversionInfo>> currencyConversion(
        @PathVariable String country,
        @RequestParam BigDecimal amount,
        @RequestParam String targetCurrency
    ) throws ResourceNotFoundException, ConflictException {
        ConversionInfo response = globalStatService.currencyConversion(country, amount, targetCurrency);
        return new ResponseEntity<>(new Response<>(response), HttpStatus.OK);
    }
}
