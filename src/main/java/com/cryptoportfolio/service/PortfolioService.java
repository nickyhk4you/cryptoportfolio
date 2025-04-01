package com.cryptoportfolio.service;

import com.cryptoportfolio.model.PutOption;
import com.cryptoportfolio.model.Security;
import com.cryptoportfolio.model.Stock;
import com.cryptoportfolio.model.CallOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cryptoportfolio.util.CsvReader;

import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PortfolioService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Map<String, BigDecimal> positions = new HashMap<>();
    private final Map<String, Security> securities = new HashMap<>();

    public void loadPositions(String csvPath) {
        try {
            // Use ClassPathResource to load the file from resources
            org.springframework.core.io.ClassPathResource resource =
                    new org.springframework.core.io.ClassPathResource(csvPath);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

            // Skip header line
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String ticker = values[0].trim();
                    BigDecimal quantity = new BigDecimal(values[1].trim());
                    positions.put(ticker, quantity);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading positions file: " + e.getMessage());
        }
    }

    public void loadSecurities() {
        String sql = "SELECT ticker, type, strike, maturity FROM securities";

        jdbcTemplate.query(sql, rs -> {
            String ticker = rs.getString("ticker");
            String type = rs.getString("type");

            Security security;
            switch (type) {
                case "STOCK":
                    security = new Stock(ticker);
                    break;
                case "CALL":
                    security = new CallOption(ticker,
                            rs.getDouble("strike"),
                            rs.getDouble("maturity"));
                    break;
                case "PUT":
                    security = new PutOption(ticker,
                            rs.getDouble("strike"),
                            rs.getDouble("maturity"));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown security type: " + type);
            }

            securities.put(ticker, security);
        });
    }

    public BigDecimal calculateNAV(Map<String, BigDecimal> marketPrices) {
        return positions.entrySet().stream()
                .map(entry -> {
                    String ticker = entry.getKey();
                    BigDecimal quantity = entry.getValue();
                    Security security = securities.get(ticker);
                    if (security == null) {
                        System.out.println("security ticket is not defined in the database :" + ticker);
                    }
                    BigDecimal price = marketPrices.getOrDefault(ticker, new BigDecimal("0.0"));
                    BigDecimal securityPrice = security.calculatePrice(price);
                    return quantity.multiply(securityPrice);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> getPositionValues(Map<String, BigDecimal> marketPrices) {
        Map<String, BigDecimal> positionValues = new HashMap<>();
        positions.forEach((ticker, quantity) -> {
            Security security = securities.get(ticker);
            BigDecimal price = marketPrices.getOrDefault(ticker, BigDecimal.ZERO);
            positionValues.put(ticker, quantity.multiply(security.calculatePrice(price)));
        });
        return positionValues;
    }
}