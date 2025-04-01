package com.cryptoportfolio.model;

import com.cryptoportfolio.util.OptionPricingUtil;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;

public class PutOption extends Security {
    private final BigDecimal strike;
    private final BigDecimal maturity;
    
    // 添加underlying_ticker字段
    private final String underlyingTicker;
    
    public PutOption(String ticker, double strike, double maturity, String underlyingTicker) {
        super(ticker);
        Preconditions.checkArgument(strike > 0, "Strike price must be positive");
        Preconditions.checkArgument(maturity > 0, "Maturity must be positive");
        Preconditions.checkNotNull(underlyingTicker, "Underlying ticker cannot be null");
        
        this.strike = BigDecimal.valueOf(strike);
        this.maturity = BigDecimal.valueOf(maturity);
        this.underlyingTicker = underlyingTicker;
        System.out.println("创建看跌期权: " + ticker + ", 行权价=" + strike + ", 到期时间=" + maturity + ", 标的资产=" + underlyingTicker);
    }
    
    public String getUnderlyingTicker() {
        return underlyingTicker;
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        System.out.println("计算看跌期权价格: " + getTicker() + ", 标的价格=" + underlyingPrice + 
                          ", 行权价=" + strike + ", 到期时间=" + maturity);
        
        if (underlyingPrice == null || underlyingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("警告: 标的价格无效, 返回零价值");
            return BigDecimal.ZERO;
        }
        
        BigDecimal result = OptionPricingUtil.calculatePutPrice(
            underlyingPrice.doubleValue(),
            strike.doubleValue(),
            maturity.doubleValue()
        );
        
        System.out.println("看跌期权计算结果: " + getTicker() + " = " + result);
        return result;
    }
}
