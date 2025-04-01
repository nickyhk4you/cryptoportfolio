package com.cryptoportfolio.model;

import java.math.BigDecimal;

/**
 * Base abstract class for all security types in the portfolio
 */
public abstract class Security {
    private final String ticker;
    
    protected Security(String ticker) {
        this.ticker = ticker;
    }
    
    public String getTicker() {
        return ticker;
    }
    
    /**
     * Calculate the price of this security based on the underlying price
     * 
     * @param underlyingPrice The price of the underlying asset
     * @return The calculated price of this security
     */
    public abstract BigDecimal calculatePrice(BigDecimal underlyingPrice);
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ticker='" + ticker + "'}";
    }
}