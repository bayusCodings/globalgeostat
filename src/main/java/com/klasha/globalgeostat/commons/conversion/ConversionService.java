package com.klasha.globalgeostat.commons.conversion;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConversionService {
    private final Map<String, Double> currencyRates = new HashMap<>();

    public ConversionService() {
        loadCurrencyRates();
    }

    private void loadCurrencyRates() {
        try {
            // Load currency rates from the CSV file
            ClassPathResource resource = new ClassPathResource("exchange_rate.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()));

            // Skip the header line
            reader.skip(1);

            String[] line;
            while ((line = reader.readNext()) != null) {
                String sourceCurrency = line[0];
                String targetCurrency = line[1];
                double rate = Double.parseDouble(line[2]);

                // Populate the map with both directions
                currencyRates.put(sourceCurrency + "_" + targetCurrency, rate);
                currencyRates.put(targetCurrency + "_" + sourceCurrency, 1.0 / rate); // Two-way conversion
            }

            reader.close();
        } catch (IOException | CsvValidationException ex) {
            ex.printStackTrace();
        }
    }

    public Double getConversionRate(String sourceCurrency, String targetCurrency) {
        return currencyRates.get(sourceCurrency + "_" + targetCurrency);
    }
}
