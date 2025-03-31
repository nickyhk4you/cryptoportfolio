package com.cryptoportfolio.model;

public class Stock extends Security {
    public Stock(String ticker) {
        super(ticker);
    }
    
    @Override
    public double calculatePrice(double underlyingPrice) {
        return underlyingPrice;
    }
}