package com.cryptoportfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PutOption extends Security {
    private final BigDecimal strike;
    private final BigDecimal maturity;
    
    public PutOption(String ticker, double strike, double maturity) {
        super(ticker);
        this.strike = BigDecimal.valueOf(strike);
        this.maturity = BigDecimal.valueOf(maturity);
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        return strike.subtract(underlyingPrice).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}
