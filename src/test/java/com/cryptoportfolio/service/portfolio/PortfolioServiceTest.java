package com.cryptoportfolio.service.portfolio;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PortfolioServiceTest {

    private PortfolioService portfolioService;
    
    @Before
    public void setUp() {
        portfolioService = new PortfolioService();
        // Initialize with test data
        portfolioService.loadPositions("test-positions.csv");
        portfolioService.loadSecurities();
    }
    
    @Test
    public void testCalculateNAV() {
        Map<String, BigDecimal> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", BigDecimal.valueOf(150.0));
        marketPrices.put("TELSA", BigDecimal.valueOf(800.0));
        
        BigDecimal nav = portfolioService.calculateNAV(marketPrices);
        assertTrue(nav.compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test
    public void testGetPositionValues() {
        Map<String, BigDecimal> marketPrices = new HashMap<>();
        marketPrices.put("AAPL", BigDecimal.valueOf(150.0));
        marketPrices.put("TELSA", BigDecimal.valueOf(800.0));
        
        Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(marketPrices);
        assertNotNull(positionValues);
        assertTrue(positionValues.containsKey("AAPL"));
        assertTrue(positionValues.get("AAPL").compareTo(BigDecimal.ZERO) > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateNAVWithNullMarketPrices() {
        portfolioService.calculateNAV(null);
    }
}