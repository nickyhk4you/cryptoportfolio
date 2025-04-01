package com.cryptoportfolio.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;

public class SecurityTest {
    @Test
    public void testStockPricing() {
        Security stock = new Stock("AAPL");
        assertEquals(0, BigDecimal.valueOf(100.0).compareTo(
            stock.calculatePrice(BigDecimal.valueOf(100.0))));
    }

    @Test
    public void testCallOptionPricing() {
        // Updated to include the underlying ticker parameter and use double for maturity
        Security call = new CallOption("AAPL", 100.0, 0.5, "AAPL_UNDERLYING");
        
        BigDecimal price = call.calculatePrice(BigDecimal.valueOf(110.0));
        assertTrue(price.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testPutOptionPricing() {
        // Updated to include the underlying ticker parameter and use double for maturity
        Security put = new PutOption("AAPL", 100.0, 0.5, "AAPL_UNDERLYING");
        
        BigDecimal price = put.calculatePrice(BigDecimal.valueOf(90.0));
        assertTrue(price.compareTo(BigDecimal.ZERO) > 0);
    }
}