package com.cryptoportfolio;

import com.cryptoportfolio.config.AppConfig;
import com.cryptoportfolio.service.ConsoleSubscriber;
import com.cryptoportfolio.service.PortfolioSubscriber;
import com.cryptoportfolio.service.market.MarketDataPublisher;
import com.cryptoportfolio.service.market.ProtoMarketDataPublisher;
import com.cryptoportfolio.service.portfolio.PortfolioService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortfolioApp {
    
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class)) {
            
            PortfolioService portfolioService = context.getBean(PortfolioService.class);
            MarketDataPublisher publisher = context.getBean(ProtoMarketDataPublisher.class);
            PortfolioSubscriber subscriber = context.getBean(ConsoleSubscriber.class);
            
            String positionsFile = context.getEnvironment()
                .getProperty("portfolio.positions.file", "positions.csv");
            portfolioService.loadPositions(positionsFile);
            portfolioService.loadSecurities();
            
            publisher.start();
            
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {
                try {
                    Map<String, BigDecimal> prices = publisher.getAllPrices();
                    BigDecimal nav = portfolioService.calculateNAV(prices);
                    Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(prices);
                    subscriber.update(nav, positionValues);
                } catch (Exception e) {
                    System.err.println("Error in portfolio calculation: " + e.getMessage());
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
            
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.err.println("Application interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}