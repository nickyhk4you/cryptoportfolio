package com.cryptoportfolio.service;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MarketDataPublisher {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();
    private double currentPrice;
    private final String ticker;
    
    public MarketDataPublisher(String ticker, double initialPrice) {
        this.ticker = ticker;
        this.currentPrice = initialPrice;
    }
    
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            // Geometric Brownian motion implementation
            double volatility = 0.2;
            double drift = 0.05;
            double dt = 0.01;
            
            double change = (drift * currentPrice * dt) + 
                          (volatility * currentPrice * random.nextGaussian() * Math.sqrt(dt));
            currentPrice += change;
            
            System.out.println("Published price for " + ticker + ": " + currentPrice);
        }, 0, 500 + random.nextInt(1500), TimeUnit.MILLISECONDS);
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
}