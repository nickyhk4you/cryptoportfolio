package com.cryptoportfolio.service;

import com.cryptoportfolio.model.Security;
import com.cryptoportfolio.model.Stock;
import com.cryptoportfolio.model.CallOption;
import com.cryptoportfolio.model.PutOption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    private Map<String, BigDecimal> marketPrices;

    @Before
    public void setup() {
        marketPrices = new HashMap<>();
        marketPrices.put("AAPL", BigDecimal.valueOf(150.0));
        portfolioService.loadPositions("positions.csv");
        portfolioService.loadSecurities();
    }

    @Test
    public void testCalculateNAV() {
        BigDecimal nav = portfolioService.calculateNAV(marketPrices);
        assertTrue("NAV should be positive", nav.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testGetPositionValues() {
        Map<String, BigDecimal> values = portfolioService.getPositionValues(marketPrices);
        assertNotNull("Position values should not be null", values);
        assertFalse("Position values should not be empty", values.isEmpty());
    }
}