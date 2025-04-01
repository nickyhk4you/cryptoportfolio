package com.cryptoportfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CallOption extends Security {
    private final BigDecimal strike;
    private final BigDecimal maturity;
    
    public CallOption(String ticker, double strike, double maturity) {
        super(ticker);
        this.strike = BigDecimal.valueOf(strike);
        this.maturity = BigDecimal.valueOf(maturity);
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        return underlyingPrice.subtract(strike).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}