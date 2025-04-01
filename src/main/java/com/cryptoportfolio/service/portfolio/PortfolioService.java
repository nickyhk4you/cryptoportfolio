package com.cryptoportfolio.service.portfolio;

import com.cryptoportfolio.model.PutOption;
import com.cryptoportfolio.model.Security;
import com.cryptoportfolio.model.Stock;
import com.cryptoportfolio.model.CallOption;
import com.cryptoportfolio.exception.PortfolioException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final Map<String, BigDecimal> positions = Maps.newHashMap();
    private final Map<String, Security> securities = Maps.newHashMap();
    
    public void loadPositions(String csvPath) {
        Preconditions.checkNotNull(csvPath, "CSV path cannot be null");
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(csvPath).getInputStream()))) {
            
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
            
            System.out.printf("Loaded %d positions from %s%n", positions.size(), csvPath);
        } catch (IOException e) {
            System.err.printf("Error reading positions file: %s%n", e.getMessage());
            throw new PortfolioException("Failed to load positions", e);
        }
    }
    
    public void loadSecurities() {
        String sql = "SELECT ticker, type, strike, maturity FROM securities";
        
        jdbcTemplate.query(sql, rs -> {
            String ticker = rs.getString("ticker");
            String type = rs.getString("type");
            
            Security security;
            switch(type) {
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
        
        System.out.printf("Loaded %d securities from database%n", securities.size());
    }
    
    public BigDecimal calculateNAV(Map<String, BigDecimal> marketPrices) {
        Preconditions.checkNotNull(marketPrices, "Market prices cannot be null");
        
        return positions.entrySet().stream()
            .map(entry -> {
                String ticker = entry.getKey();
                BigDecimal quantity = entry.getValue();
                
                Security security = Optional.ofNullable(securities.get(ticker))
                    .orElseThrow(() -> {
                        System.out.printf("Security not defined in database: %s%n", ticker);
                        return new IllegalStateException("Security not found: " + ticker);
                    });
                
                BigDecimal price = marketPrices.getOrDefault(ticker, BigDecimal.ZERO);
                BigDecimal securityPrice = security.calculatePrice(price);
                
                return quantity.multiply(securityPrice);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> getPositionValues(Map<String, BigDecimal> marketPrices) {
        Preconditions.checkNotNull(marketPrices, "Market prices cannot be null");
        
        Map<String, BigDecimal> positionValues = Maps.newHashMap();
        
        positions.forEach((ticker, quantity) -> {
            Security security = securities.get(ticker);
            
            if (security == null) {
                System.out.printf("Security not defined in database: %s%n", ticker);
                positionValues.put(ticker, BigDecimal.ZERO);
                return;
            }
            
            BigDecimal price = marketPrices.getOrDefault(ticker, BigDecimal.ZERO);
            positionValues.put(ticker, quantity.multiply(security.calculatePrice(price)));
        });
        
        return ImmutableMap.copyOf(positionValues);
    }
}