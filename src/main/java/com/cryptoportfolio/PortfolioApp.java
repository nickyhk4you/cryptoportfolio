package com.cryptoportfolio;

import com.cryptoportfolio.config.AppConfig;
import com.cryptoportfolio.service.MarketDataPublisher;
import com.cryptoportfolio.service.PortfolioService;
import com.cryptoportfolio.service.ConsoleSubscriber;
import com.cryptoportfolio.service.PortfolioSubscriber;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

public class PortfolioApp {
    private static final Map<String, BigDecimal> marketPrices = new HashMap<>();
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        PortfolioService portfolioService = context.getBean(PortfolioService.class);
        
        portfolioService.loadPositions("positions.csv");
        portfolioService.loadSecurities();
        
        MarketDataPublisher publisher = new MarketDataPublisher();
        publisher.start();
        
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        PortfolioSubscriber subscriber = new ConsoleSubscriber();
        
        executor.scheduleAtFixedRate(() -> {
            System.out.println("\n## calculating...");
            Map<String, BigDecimal> prices = publisher.getAllPrices();
            BigDecimal nav = portfolioService.calculateNAV(prices);
            Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(prices);
            
            subscriber.update(nav, positionValues);
        }, 0, 1000, TimeUnit.MILLISECONDS);
    
        // 添加这段代码防止程序退出
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            executor.shutdown();
            publisher.stop();
            context.close();
        }
    }
}