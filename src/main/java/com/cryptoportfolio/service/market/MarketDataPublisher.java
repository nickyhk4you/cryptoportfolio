package com.cryptoportfolio.service.market;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MarketDataPublisher implements DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(MarketDataPublisher.class);
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1,
            new ThreadFactoryBuilder().setNameFormat("market-data-publisher-%d").build());
    
    private final Random random = new Random();
    private final Map<String, BigDecimal> currentPrices = Maps.newConcurrentMap();
    
    @Value("${market.data.volatility:0.2}")
    private double volatility;
    
    @Value("${market.data.drift:0.05}")
    private double drift;
    
    @Value("${market.data.timeStep:0.01}")
    private double timeStep;
    
    public MarketDataPublisher() {
        // Initialize with some default prices
        currentPrices.put("AAPL", BigDecimal.valueOf(110.0));
        currentPrices.put("TELSA", BigDecimal.valueOf(450.0));
    }
    
    /**
     * Starts publishing market data at regular intervals
     */
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("Updating market prices");
            
            currentPrices.forEach((ticker, price) -> {
                BigDecimal volatilityFactor = BigDecimal.valueOf(volatility);
                BigDecimal driftFactor = BigDecimal.valueOf(drift);
                BigDecimal dt = BigDecimal.valueOf(timeStep);
                
                BigDecimal randomFactor = BigDecimal.valueOf(random.nextGaussian());
                BigDecimal change = driftFactor.multiply(price).multiply(dt)
                    .add(volatilityFactor.multiply(price).multiply(randomFactor)
                         .multiply(BigDecimal.valueOf(Math.sqrt(timeStep))));
                
                currentPrices.put(ticker, price.add(change).setScale(2, RoundingMode.HALF_UP));
            });
            
            logger.debug("Updated prices: {}", currentPrices);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Stops the market data publisher
     */
    public void stop() {
        logger.info("Stopping market data publisher");
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
    
    /**
     * Gets the current price for a ticker
     * 
     * @param ticker The ticker symbol
     * @return The current price
     */
    public BigDecimal getCurrentPrice(String ticker) {
        return currentPrices.getOrDefault(ticker, BigDecimal.ZERO);
    }
    
    /**
     * Gets all current prices
     * 
     * @return Map of ticker to price
     */
    public Map<String, BigDecimal> getAllPrices() {
        return ImmutableMap.copyOf(currentPrices);
    }
    
    @Override
    public void destroy() {
        stop();
    }
}