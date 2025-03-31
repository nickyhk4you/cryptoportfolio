package com.cryptoportfolio.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class SecurityTest {

    @Test
    public void testStockPricing() {
        Security stock = new Stock("AAPL");
        assertEquals(100.0, stock.calculatePrice(100.0), 0.001);
    }

    @Test
    public void testCallOptionPricing() {
        Security call = new CallOption("AAPL", 100.0, 20231231);
        assertEquals(50.0, call.calculatePrice(150.0), 0.001);
        assertEquals(0.0, call.calculatePrice(50.0), 0.001);
    }

    @Test
    public void testPutOptionPricing() {
        Security put = new PutOption("AAPL", 100.0, 20231231);
        assertEquals(50.0, put.calculatePrice(50.0), 0.001);
        assertEquals(0.0, put.calculatePrice(150.0), 0.001);
    }
}