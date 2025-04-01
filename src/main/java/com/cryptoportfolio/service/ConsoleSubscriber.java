package com.cryptoportfolio.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class ConsoleSubscriber implements PortfolioSubscriber {
    private static int updateCount = 0;

    @Override
    public void update(BigDecimal nav, Map<String, BigDecimal> positionValues) {
        try {
            updateCount++;
            System.out.println("\n## " + updateCount + " Market Data Update");
            
            System.out.println("NAV: " + nav);
            System.out.println("Position Values: " + positionValues);
            
            System.out.println("Market Data Update:");
            if (positionValues != null) {
                positionValues.forEach((ticker, value) -> {
                    if (ticker.equals("AAPL") || ticker.equals("TELSA")) {
                        try {
                            System.out.printf("%s change to %.2f%n", ticker, 
                                value.divide(getQuantity(ticker), 2, RoundingMode.HALF_UP));
                        } catch (Exception e) {
                            System.out.println("Error processing " + ticker + ": " + e.getMessage());
                        }
                    }
                });
            } else {
                System.out.println("Position values is null!");
            }

            System.out.println("\nPortfolio Details:");
            System.out.printf("%-25s %12s %15s %15s%n", "symbol", "price", "qty", "value");
            
            if (positionValues != null) {
                positionValues.forEach((ticker, value) -> {
                    try {
                        BigDecimal qty = getQuantity(ticker);
                        BigDecimal price = value.divide(qty, 2, RoundingMode.HALF_UP);
                        System.out.printf("%-25s %12.2f %15.2f %15.2f%n",
                            ticker, price, qty, value);
                    } catch (Exception e) {
                        System.out.println("Error processing " + ticker + ": " + e.getMessage());
                    }
                });
            }

            System.out.printf("\nTotal Portfolio Value: %.2f%n", nav);
        } catch (Exception e) {
            System.out.println("ERROR in ConsoleSubscriber.update(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private BigDecimal getQuantity(String ticker) {
        switch (ticker) {
            case "AAPL": return BigDecimal.valueOf(1000.00);
            case "AAPL-OCT-2020-110-C": return BigDecimal.valueOf(-20000.00);
            case "AAPL-OCT-2020-110-P": return BigDecimal.valueOf(20000.00);
            case "TELSA": return BigDecimal.valueOf(-500.00);
            case "TELSA-NOV-2020-400-C": return BigDecimal.valueOf(10000.00);
            case "TELSA-DEC-2020-400-P": return BigDecimal.valueOf(-10000.00);
            default: return BigDecimal.ONE; // 避免除零错误
        }
    }
}