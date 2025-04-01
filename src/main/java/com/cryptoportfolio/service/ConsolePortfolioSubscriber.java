package com.cryptoportfolio.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class ConsolePortfolioSubscriber implements PortfolioSubscriber {

    @Override
    public void update(BigDecimal nav, Map<String, BigDecimal> positionValues) {
        System.out.println("\n## Portfolio");
        System.out.printf("%-25s %15s %15s %15s%n", "symbol", "price", "qty", "value");
        
        positionValues.forEach((ticker, value) -> {
            System.out.printf("%-25s %15.2f %15.2f %15.2f%n",
                ticker,
                value.doubleValue(),
                0.0, // TODO: Add quantity information
                value.doubleValue());
        });
        
        System.out.printf("%nTotal portfolio: %15.2f%n", nav);
    }
}