package com.cryptoportfolio.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 添加 Guava 导入
import com.google.common.base.Preconditions;

public class PutOption extends Security {
    private final BigDecimal strike;
    private final BigDecimal maturity;
    
    public PutOption(String ticker, double strike, double maturity) {
        super(ticker);
        Preconditions.checkArgument(strike > 0, "Strike price must be positive");
        Preconditions.checkArgument(maturity > 0, "Maturity must be positive");
        
        this.strike = BigDecimal.valueOf(strike);
        this.maturity = BigDecimal.valueOf(maturity);
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        Preconditions.checkNotNull(underlyingPrice, "Underlying price cannot be null");
        return strike.subtract(underlyingPrice).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
    }
}
