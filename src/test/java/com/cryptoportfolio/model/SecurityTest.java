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
        Security call = new CallOption("AAPL", 100.0, 20231231);
        assertEquals(0, BigDecimal.valueOf(50.0).compareTo(
            call.calculatePrice(BigDecimal.valueOf(150.0))));
        assertEquals(0, BigDecimal.ZERO.compareTo(
            call.calculatePrice(BigDecimal.valueOf(50.0))));
    }

    @Test
    public void testPutOptionPricing() {
        Security put = new PutOption("AAPL", 100.0, 20231231);
        assertEquals(0, BigDecimal.valueOf(50.0).compareTo(
            put.calculatePrice(BigDecimal.valueOf(50.0))));
        assertEquals(0, BigDecimal.ZERO.compareTo(
            put.calculatePrice(BigDecimal.valueOf(150.0))));
    }
}