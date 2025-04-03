package com.cryptoportfolio;

import com.cryptoportfolio.service.ConsoleSubscriber;
import com.cryptoportfolio.service.MarketDataService;
import com.cryptoportfolio.service.market.MarketDataConnector;
import com.cryptoportfolio.service.market.MarketDataPublisher;
import com.cryptoportfolio.service.portfolio.PortfolioService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan("com.cryptoportfolio")
public class PortfolioApp {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(PortfolioApp.class)) {
            
            // Get service instances
            MarketDataService marketDataService = context.getBean(MarketDataService.class);
            // Specify which MarketDataPublisher bean to use
            MarketDataPublisher marketDataPublisher = context.getBean("marketDataPublisher", MarketDataPublisher.class);
            ConsoleSubscriber consoleSubscriber = context.getBean(ConsoleSubscriber.class);
            PortfolioService portfolioService = context.getBean(PortfolioService.class);
            
            // Load securities and position data
            System.out.println("Loading portfolio data...");
            portfolioService.loadSecurities();
            portfolioService.loadPositions("positions.csv");
            
            // Subscribe to market data updates
            System.out.println("Subscribing to market data updates...");
            marketDataService.subscribe(consoleSubscriber);
            
            // Start market data publisher
            System.out.println("Starting market data publisher...");
            marketDataPublisher.start();
            
            // Create connector to link market data publisher with market data service
            MarketDataConnector connector = new MarketDataConnector(marketDataPublisher, marketDataService);
            connector.start();
            
            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(5)); // Run for 5 minutes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}