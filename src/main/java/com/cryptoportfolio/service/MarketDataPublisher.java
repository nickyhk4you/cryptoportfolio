package com.cryptoportfolio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 添加 Guava 导入
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class MarketDataPublisher {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
            new ThreadFactoryBuilder().setNameFormat("market-data-publisher-%d").build());
    private final Random random = new Random();
    private final Map<String, BigDecimal> currentPrices = Maps.newConcurrentMap();
    
    public MarketDataPublisher() {
        currentPrices.put("AAPL", BigDecimal.valueOf(110.0));
        currentPrices.put("TELSA", BigDecimal.valueOf(450.0));
    }
    
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n## update price");

            currentPrices.forEach((ticker, price) -> {
                BigDecimal volatility = BigDecimal.valueOf(0.2);
                BigDecimal drift = BigDecimal.valueOf(0.05);
                BigDecimal dt = BigDecimal.valueOf(0.01);
                
                BigDecimal randomFactor = BigDecimal.valueOf(random.nextGaussian());
                BigDecimal change = drift.multiply(price).multiply(dt)
                    .add(volatility.multiply(price).multiply(randomFactor).multiply(BigDecimal.valueOf(Math.sqrt(0.01))));
                
                currentPrices.put(ticker, price.add(change).setScale(2, RoundingMode.HALF_UP));
            });
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
    
    public void stop() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public BigDecimal getCurrentPrice(String ticker) {
        return currentPrices.getOrDefault(ticker, BigDecimal.ZERO);
    }
    
    public Map<String, BigDecimal> getAllPrices() {
        return ImmutableMap.copyOf(currentPrices);
    }
}