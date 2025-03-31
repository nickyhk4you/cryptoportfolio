package com.cryptoportfolio.service;

import org.junit.Test;
import static org.junit.Assert.*;

public class MarketDataPublisherTest {

    @Test
    public void testInitialPrice() {
        MarketDataPublisher publisher = new MarketDataPublisher("AAPL", 150.0);
        assertEquals(150.0, publisher.getCurrentPrice(), 0.001);
    }

    @Test
    public void testPriceUpdate() {
        MarketDataPublisher publisher = new MarketDataPublisher("AAPL", 150.0);
        publisher.start();
        double initialPrice = publisher.getCurrentPrice();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        double updatedPrice = publisher.getCurrentPrice();
        assertNotEquals(initialPrice, updatedPrice);
        publisher.stop();
    }
}