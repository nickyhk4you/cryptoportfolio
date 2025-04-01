package com.cryptoportfolio;

import com.cryptoportfolio.config.AppConfig;
import com.cryptoportfolio.service.ConsoleSubscriber;
import com.cryptoportfolio.service.market.MarketDataPublisher;
import com.cryptoportfolio.service.portfolio.PortfolioService;
import com.cryptoportfolio.service.PortfolioSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortfolioApp {
    private static final Logger logger = LoggerFactory.getLogger(PortfolioApp.class);
    
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(AppConfig.class)) {
            
            PortfolioService portfolioService = context.getBean(PortfolioService.class);
            MarketDataPublisher publisher = context.getBean(MarketDataPublisher.class);
            PortfolioSubscriber subscriber = context.getBean(ConsoleSubscriber.class);
            
            // Load portfolio data
            String positionsFile = context.getEnvironment()
                .getProperty("portfolio.positions.file", "positions.csv");
            portfolioService.loadPositions(positionsFile);
            portfolioService.loadSecurities();
            
            // Start market data publisher
            publisher.start();
            
            // Schedule portfolio updates
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {
                try {
                    logger.info("Calculating portfolio values");
                    
                    Map<String, BigDecimal> prices = publisher.getAllPrices();
                    logger.info("Market prices: {}", prices);
                    
                    BigDecimal nav = portfolioService.calculateNAV(prices);
                    logger.info("Portfolio NAV: {}", nav);
                    
                    Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(prices);
                    logger.info("Position values: {}", positionValues);
                    
                    subscriber.update(nav, positionValues);
                } catch (Exception e) {
                    logger.error("Error in portfolio calculation: {}", e.getMessage(), e);
                }
            }, 0, 1000, TimeUnit.MILLISECONDS);
            
            // Keep application running
            Thread.sleep(Long.MAX_VALUE);
            
            // Cleanup
            executor.shutdown();
            publisher.stop();
        } catch (InterruptedException e) {
            logger.error("Application interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}