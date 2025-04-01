package com.cryptoportfolio.model;

import java.math.BigDecimal;

public abstract class Security {
    private String ticker;
    
    public Security(String ticker) {
        this.ticker = ticker;
    }
    
    public String getTicker() {
        return ticker;
    }
    
    public abstract BigDecimal calculatePrice(BigDecimal underlyingPrice);
}