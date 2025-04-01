package com.cryptoportfolio.service;

import com.cryptoportfolio.model.PutOption;
import com.cryptoportfolio.model.Security;
import com.cryptoportfolio.model.Stock;
import com.cryptoportfolio.model.CallOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

// 添加 Guava 导入
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

@Service
public class PortfolioService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Map<String, BigDecimal> positions = Maps.newHashMap();
    private final Map<String, Security> securities = Maps.newHashMap();

    public void loadPositions(String csvPath) {
        Preconditions.checkNotNull(csvPath, "CSV path cannot be null");
        
        try {
            // 使用 ClassPathResource 从资源加载文件
            org.springframework.core.io.ClassPathResource resource =
                    new org.springframework.core.io.ClassPathResource(csvPath);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));

            // 跳过标题行
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String ticker = values[0].trim();
                    BigDecimal quantity = new BigDecimal(values[1].trim());
                    positions.put(ticker, quantity);
                }
            }
            
            // 打印加载的仓位信息
            System.out.println("已加载仓位: " + positions);
        } catch (IOException e) {
            System.err.println("Error reading positions file: " + e.getMessage());
        }
    }

    public void loadSecurities() {
        String sql = "SELECT ticker, type, strike, maturity FROM securities";

        jdbcTemplate.query(sql, rs -> {
            String ticker = rs.getString("ticker");
            String type = rs.getString("type");

            Security security;
            switch (type) {
                case "STOCK":
                    security = new Stock(ticker);
                    break;
                case "CALL":
                    security = new CallOption(ticker,
                            rs.getDouble("strike"),
                            rs.getDouble("maturity"));
                    break;
                case "PUT":
                    security = new PutOption(ticker,
                            rs.getDouble("strike"),
                            rs.getDouble("maturity"));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown security type: " + type);
            }

            securities.put(ticker, security);
        });
        
        // 打印加载的证券信息
        System.out.println("已加载证券: " + securities.keySet());
    }

    public BigDecimal calculateNAV(Map<String, BigDecimal> marketPrices) {
        Preconditions.checkNotNull(marketPrices, "Market prices cannot be null");
        
        return positions.entrySet().stream()
                .map(entry -> {
                    String ticker = entry.getKey();
                    BigDecimal quantity = entry.getValue();
                    Security security = securities.get(ticker);
                    
                    if (security == null) {
                        System.out.println("证券未在数据库中定义: " + ticker);
                        return BigDecimal.ZERO;
                    }
                    
                    BigDecimal price = marketPrices.getOrDefault(ticker, BigDecimal.ZERO);
                    BigDecimal securityPrice = security.calculatePrice(price);
                    return quantity.multiply(securityPrice);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> getPositionValues(Map<String, BigDecimal> marketPrices) {
        Preconditions.checkNotNull(marketPrices, "Market prices cannot be null");
        
        Map<String, BigDecimal> positionValues = Maps.newHashMap();
        
        positions.forEach((ticker, quantity) -> {
            Security security = securities.get(ticker);
            
            if (security == null) {
                System.out.println("证券未在数据库中定义: " + ticker);
                positionValues.put(ticker, BigDecimal.ZERO);
                return;
            }
            
            BigDecimal price = marketPrices.getOrDefault(ticker, BigDecimal.ZERO);
            positionValues.put(ticker, quantity.multiply(security.calculatePrice(price)));
        });
        
        // 返回不可变的 Map
        return ImmutableMap.copyOf(positionValues);
    }
}