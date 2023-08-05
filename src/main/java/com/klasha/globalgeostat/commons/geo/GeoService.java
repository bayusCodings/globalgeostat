package com.klasha.globalgeostat.commons.geo;

import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalData;
import com.klasha.globalgeostat.commons.geo.dto.capital.CapitalResponse;
import com.klasha.globalgeostat.commons.geo.dto.city.CityResponse;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyData;
import com.klasha.globalgeostat.commons.geo.dto.currency.CurrencyResponse;
import com.klasha.globalgeostat.commons.geo.dto.population.CityPopulationResponse;
import com.klasha.globalgeostat.commons.geo.dto.population.CityPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationData;
import com.klasha.globalgeostat.commons.geo.dto.population.CountryPopulationResponse;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionData;
import com.klasha.globalgeostat.commons.geo.dto.position.PositionResponse;
import com.klasha.globalgeostat.commons.geo.dto.state.StateData;
import com.klasha.globalgeostat.commons.geo.dto.state.StateResponse;
import com.klasha.globalgeostat.core.exception.GeoServiceException;
import com.klasha.globalgeostat.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {
    private final RestTemplate restTemplate;

    @Value("${service.geo.baseUrl}")
    String baseUrl;

    public List<CityPopulationData> getCityPopulation(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/population/cities/filter/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<CityPopulationResponse> response = restTemplate.getForEntity(uri, CityPopulationResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getCityPopulation error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve city population for %s", country));
            }
            throw new GeoServiceException("Error while fetching city data; please try again");
        }
    }

    public CountryPopulationData getCountryPopulation(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/population/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<CountryPopulationResponse> response = restTemplate.getForEntity(uri, CountryPopulationResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getCountryPopulation error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve country population for %s", country));
            }
            throw new GeoServiceException("Error while fetching country population; please try again");
        }
    }

    public CapitalData getCapital(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/capital/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<CapitalResponse> response = restTemplate.getForEntity(uri, CapitalResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getCapital error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve capital for %s", country));
            }
            throw new GeoServiceException("Error while fetching capital; please try again");
        }
    }

    public PositionData getLocation(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/positions/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<PositionResponse> response = restTemplate.getForEntity(uri, PositionResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getLocation error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve location for %s", country));
            }
            throw new GeoServiceException("Error while fetching location; please try again");
        }
    }

    public CurrencyData getCurrency(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/currency/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<CurrencyResponse> response = restTemplate.getForEntity(uri, CurrencyResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getCurrency error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve currency details for %s", country));
            }
            throw new GeoServiceException("Error while fetching currency; please try again");
        }
    }

    public StateData getStates(String country) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/states/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .build()
                .toUriString();

            ResponseEntity<StateResponse> response = restTemplate.getForEntity(uri, StateResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getStates error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve states for %s", country));
            }
            throw new GeoServiceException("Error while fetching states; please try again");
        }
    }

    public List<String> getCities(String country, String state) throws GeoServiceException, ResourceNotFoundException {
        try {
            String requestUrl = String.format("%s/cities/q", baseUrl);
            String uri = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .queryParam("country", country)
                .queryParam("state", state)
                .build()
                .toUriString();

            ResponseEntity<CityResponse> response = restTemplate.getForEntity(uri, CityResponse.class);
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            log.error("getCities error: {} status code: {}", ex.getResponseBodyAsString(), statusCode);

            if (statusCode.equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException(String.format("Could not retrieve cities under %s", state));
            }
            throw new GeoServiceException("Error while fetching cities; please try again");
        }
    }
}
