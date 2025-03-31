package com.cryptoportfolio.service;

import java.util.Map;

public class ConsoleSubscriber implements PortfolioSubscriber {
    @Override
    public void update(double nav, Map<String, Double> positionValues) {
        System.out.println("\n=== Portfolio Update ===");
        positionValues.forEach((ticker, value) -> 
            System.out.printf("%s Value: $%.2f%n", ticker, value));
        System.out.printf("Portfolio NAV: $%.2f%n", nav);
        System.out.println("=======================");
    }
}