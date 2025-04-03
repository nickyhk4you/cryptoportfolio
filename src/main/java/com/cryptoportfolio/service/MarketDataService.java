package com.cryptoportfolio.service;

import com.cryptoportfolio.service.portfolio.PortfolioService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Service
public class MarketDataService {
    
    // Replace HashSet with Guava's Sets.newConcurrentHashSet()
    private final Set<PortfolioSubscriber> subscribers = Sets.newConcurrentHashSet();
    
    @Autowired
    private PortfolioService portfolioService;
    
    public void subscribe(PortfolioSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    
    public void unsubscribe(PortfolioSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
    
    public void processMarketDataUpdate(Map<String, BigDecimal> marketData) {
        // Create an immutable copy of the market data
        Map<String, BigDecimal> immutableMarketData = ImmutableMap.copyOf(marketData);
        
        // Calculate NAV and position values
        BigDecimal nav = portfolioService.calculateNAV(immutableMarketData);
        Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(immutableMarketData);
        
        // Notify subscribers
        for (PortfolioSubscriber subscriber : subscribers) {
            subscriber.update(nav, positionValues);
        }
    }
}