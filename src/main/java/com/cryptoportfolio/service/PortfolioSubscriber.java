package com.cryptoportfolio.service;

import java.util.Map;

public interface PortfolioSubscriber {
    void update(double nav, Map<String, Double> positionValues);
}