package com.cryptoportfolio.model;

public class CallOption extends Security {
    private double strike;
    private double maturity;
    
    public CallOption(String ticker, double strike, double maturity) {
        super(ticker);
        this.strike = strike;
        this.maturity = maturity;
    }
    
    @Override
    public double calculatePrice(double underlyingPrice) {
        // Black-Scholes would go here, simplified for now
        return Math.max(0, underlyingPrice - strike);
    }
}