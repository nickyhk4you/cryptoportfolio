package com.cryptoportfolio.model;

public abstract class Security {
    private String ticker;
    
    public Security(String ticker) {
        this.ticker = ticker;
    }
    
    public String getTicker() {
        return ticker;
    }
    
    public abstract double calculatePrice(double underlyingPrice);
}