package com.cryptoportfolio.service.market;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataPublisher {
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();
    private final Map<String, BigDecimal> currentPrices = Maps.newConcurrentMap();
    private int updateCount = 0;

    public MarketDataPublisher() {
        scheduler = Executors.newScheduledThreadPool(1,
                new ThreadFactoryBuilder().setNameFormat("market-data-publisher-%d").build());
        
        // Initialize with default prices
        currentPrices.put("AAPL", BigDecimal.valueOf(110.0));
        currentPrices.put("TELSA", BigDecimal.valueOf(450.0));
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            updateCount++;
            System.out.printf("%n## %d Market Data Update%n", updateCount);
            
            currentPrices.forEach((ticker, price) -> {
                double volatility = 0.2;
                double drift = 0.05;
                double dt = 0.01;
                
                double randomFactor = random.nextGaussian();
                BigDecimal change = price.multiply(BigDecimal.valueOf(drift * dt + volatility * randomFactor * Math.sqrt(dt)));
                BigDecimal newPrice = price.add(change).setScale(2, RoundingMode.HALF_UP);
                
                currentPrices.put(ticker, newPrice);
                System.out.printf("%s change to %.2f%n", ticker, newPrice);
            });
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Gets all current prices
     * 
     * @return Map of ticker to price
     */
    public Map<String, BigDecimal> getAllPrices() {
        return ImmutableMap.copyOf(currentPrices);
    }
}