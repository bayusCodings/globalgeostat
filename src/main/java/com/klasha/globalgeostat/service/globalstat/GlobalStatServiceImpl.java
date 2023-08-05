package com.klasha.globalgeostat.service.globalstat;

import com.klasha.globalgeostat.commons.Countries;
import com.klasha.globalgeostat.commons.conversion.ConversionService;
import com.klasha.globalgeostat.commons.geo.GeoService;
import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalData;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyData;
import com.klasha.globalgeostat.commons.geo.dto.population.CityPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;
import com.klasha.globalgeostat.commons.geo.dto.state.StateData;
import com.klasha.globalgeostat.core.exception.ConflictException;
import com.klasha.globalgeostat.core.exception.GeoServiceException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import com.klasha.globalgeostat.service.globalstat.dto.ConversionInfo;
import com.klasha.globalgeostat.service.globalstat.dto.CountryInfo;
import com.klasha.globalgeostat.service.globalstat.dto.StateInfo;
import com.klasha.globalgeostat.service.globalstat.mapper.StatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlobalStatServiceImpl implements GlobalStatService {
    private final GeoService geoService;
    private final ConversionService conversionService;

    @Override
    public List<String> getMostPopulatedCities(int N) {
        // List of countries to fetch city population data from
        List<String> countries = List.of(Countries.ITALY, Countries.NEW_ZEALAND, Countries.GHANA);

        // List to store CompletableFuture instances for parallel fetching
        List<CompletableFuture<List<CityPopulationData>>> futures = new ArrayList<>();

        // Fetch city population data for each country asynchronously
        for (String country : countries) {
            CompletableFuture<List<CityPopulationData>> future = fetchCityPopulationData(country);
            futures.add(future);
        }

        // Wait for all CompletableFuture instances to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();

        // Combine the results from CompletableFuture instances
        List<CityPopulationData> combinedCityPopulationData = new ArrayList<>();
        for (CompletableFuture<List<CityPopulationData>> future : futures) {
            combinedCityPopulationData.addAll(future.join());
        }

        // Sort the combined city data by population in descending order
        combinedCityPopulationData.sort(Comparator.comparing(
            cityData -> Integer.parseInt(cityData.getPopulationCounts().get(0).getValue()),
            Comparator.reverseOrder()));

        // Get a sublist of the most populated cities up to N
        List<CityPopulationData> subList = combinedCityPopulationData
            .subList(0, Math.min(N, combinedCityPopulationData.size()));

        // Extract and return only city names from the sublist
        return subList.stream().map(CityPopulationData::getCity).collect(Collectors.toList());
    }

    @Override
    public CountryInfo getCountryInfo(String country) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        CompletableFuture<CountryPopulationData> countryPopulationDataFuture = fetchCountryPopulationData(country);
        CompletableFuture<CapitalData> capitalDataFuture = fetchCapitalData(country);
        CompletableFuture<PositionData> positionDataFuture = fetchPositionData(country);
        CompletableFuture<CurrencyData> currencyDataFuture = fetchCurrencyData(country);

        // Add all the futures to the list
        futures.add(countryPopulationDataFuture);
        futures.add(capitalDataFuture);
        futures.add(positionDataFuture);
        futures.add(currencyDataFuture);

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Use join() to wait for all futures to complete
        allOf.join();

        return StatMapper.mapToCountryInfo(
            countryPopulationDataFuture.join(),
            capitalDataFuture.join(),
            positionDataFuture.join(),
            currencyDataFuture.join()
        );
    }

    @Override
    public List<StateInfo> getStateInfo(String country) {
        CompletableFuture<StateData> stateDataFuture = fetchStateData(country);
        StateData stateData = stateDataFuture.join();

        List<CompletableFuture<StateInfo>> stateInfoFutures = stateData.getStates().stream().map(
            state -> fetchCities(country, state.getName()).thenApply(
                cities -> StatMapper.mapToStateInfo(state.getName(), state.getStateCode(), cities)
            )
        ).toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(stateInfoFutures.toArray(new CompletableFuture[0]));
        allFutures.join();

        return stateInfoFutures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    @Override
    public ConversionInfo currencyConversion(String country, BigDecimal amount, String targetCurrency)
        throws ResourceNotFoundException, ConflictException {
        CompletableFuture<CurrencyData> currencyDataFuture = fetchCurrencyData(country);
        CurrencyData currencyData = currencyDataFuture.join();
        String currency = currencyData.getCurrency();

        if (currency.equals(targetCurrency)) {
            throw new ConflictException("Conflict in source and target currency");
        }

        Double conversionRate = conversionService.getConversionRate(currency, targetCurrency);
        if (conversionRate == null) {
            throw new ResourceNotFoundException(String.format("No conversion rate found for %s", currency));
        }

        BigDecimal convertedAmount = amount.multiply(new BigDecimal(conversionRate));

        // Format the result
        BigDecimal formattedValue = convertedAmount.setScale(2, RoundingMode.HALF_UP);
        return StatMapper.mapToConversionInfo(currency, formattedValue);
    }

    private CompletableFuture<List<CityPopulationData>> fetchCityPopulationData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getCityPopulation(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<CountryPopulationData> fetchCountryPopulationData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getCountryPopulation(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<CapitalData> fetchCapitalData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getCapital(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<PositionData> fetchPositionData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getLocation(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<CurrencyData> fetchCurrencyData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getCurrency(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<StateData> fetchStateData(String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getStates(country);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CompletableFuture<List<String>> fetchCities(String country, String state) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoService.getCities(country, state);
            } catch (GeoServiceException | ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
