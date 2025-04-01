package com.cryptoportfolio.model;

import java.math.BigDecimal;

public class Stock extends Security {
    public Stock(String ticker) {
        super(ticker);
    }
    
    @Override
    public BigDecimal calculatePrice(BigDecimal underlyingPrice) {
        return underlyingPrice;
    }
}