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
            try {
                System.out.println("\n## 开始计算...");
                Map<String, BigDecimal> prices = publisher.getAllPrices();
                System.out.println("获取到价格: " + prices);
                
                BigDecimal nav = portfolioService.calculateNAV(prices);
                System.out.println("计算NAV: " + nav);
                
                Map<String, BigDecimal> positionValues = portfolioService.getPositionValues(prices);
                System.out.println("计算仓位价值: " + positionValues);
                
                subscriber.update(nav, positionValues);
            } catch (Exception e) {
                System.out.println("ERROR in scheduled task: " + e.getMessage());
                e.printStackTrace();
            }
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