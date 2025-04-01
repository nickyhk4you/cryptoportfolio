package com.cryptoportfolio.service;

import java.math.BigDecimal;
import java.util.Map;

public interface PortfolioSubscriber {
    void update(BigDecimal nav, Map<String, BigDecimal> positionValues);
}