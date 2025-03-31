package com.cryptoportfolio;

import com.cryptoportfolio.config.AppConfig;
import com.cryptoportfolio.service.MarketDataPublisher;
import com.cryptoportfolio.service.PortfolioService;
import com.cryptoportfolio.service.ConsoleSubscriber;
import com.cryptoportfolio.service.PortfolioSubscriber;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class PortfolioApp {
    private static final Map<String, Double> marketPrices = new HashMap<>();
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        PortfolioService portfolioService = context.getBean(PortfolioService.class);
        
        portfolioService.loadPositions("positions.csv");  // This will now look in src/main/resources
        portfolioService.loadSecurities();
        
        MarketDataPublisher aaplPublisher = new MarketDataPublisher("AAPL", 150.0);
        aaplPublisher.start();
        
        // Portfolio subscriber setup
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        PortfolioSubscriber subscriber = new ConsoleSubscriber();
        
        executor.scheduleAtFixedRate(() -> {
            marketPrices.put("AAPL", aaplPublisher.getCurrentPrice());
            double nav = portfolioService.calculateNAV(marketPrices);
            Map<String, Double> positionValues = portfolioService.getPositionValues(marketPrices);
            
            subscriber.update(nav, positionValues);
        }, 1, 1, TimeUnit.SECONDS);
    }
}