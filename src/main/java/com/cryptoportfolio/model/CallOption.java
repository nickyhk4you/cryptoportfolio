package com.cryptoportfolio.model;

import com.cryptoportfolio.util.OptionPricingUtil;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;

public class CallOption extends Security {
    private final BigDecimal strike;
    private final BigDecimal maturity;
    private final String underlyingTicker;
    
    public CallOption(String ticker, double strike, double maturity, String underlyingTicker) {
        super(ticker);
        Preconditions.checkArgument(strike > 0, "Strike price must be positive");
        Preconditions.checkArgument(maturity > 0, "Maturity must be positive");
        Preconditions.checkNotNull(underlyingTicker, "Underlying ticker cannot be null");
        
        this.strike = BigDecimal.valueOf(strike);
        this.maturity = BigDecimal.valueOf(maturity);
        this.underlyingTicker = underlyingTicker;
        System.out.println("Creating call option: " + ticker + ", strike=" + strike + ", maturity=" + maturity + ", underlying=" + underlyingTicker);
    }
    
    public String getUnderlyingTicker() {
        return underlyingTicker;
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        if (underlyingPrice == null) {
            throw new NullPointerException("Underlying price cannot be null");
        }
        
        if (underlyingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        return OptionPricingUtil.calculateCallPrice(
            underlyingPrice.doubleValue(),
            strike.doubleValue(),
            maturity.doubleValue()
        );
    }
}