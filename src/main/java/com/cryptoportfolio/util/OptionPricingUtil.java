package com.cryptoportfolio.util;

import org.apache.commons.math3.distribution.NormalDistribution;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class OptionPricingUtil {
    private static final double RISK_FREE_RATE = 0.02; // 2% 年利率
    private static final double VOLATILITY = 0.30; // 30% 年波动率
    private static final NormalDistribution NORMAL = new NormalDistribution();
    
    public static BigDecimal calculateCallPrice(double stockPrice, double strikePrice, double timeToMaturity) {
        System.out.println("OptionPricingUtil - 计算看涨期权: S=" + stockPrice + ", K=" + strikePrice + ", t=" + timeToMaturity);
        
        // 检查参数有效性
        if (stockPrice <= 0 || strikePrice <= 0 || timeToMaturity <= 0) {
            System.out.println("无效的期权参数, 返回零价值");
            return BigDecimal.ZERO;
        }
        
        try {
            double S = stockPrice;
            double K = strikePrice;
            double t = timeToMaturity;
            double r = RISK_FREE_RATE;
            double sigma = VOLATILITY;
            
            double d1 = calculateD1(S, K, t, r, sigma);
            double d2 = d1 - sigma * Math.sqrt(t);
            
            System.out.println("d1=" + d1 + ", d2=" + d2);
            
            double nd1 = NORMAL.cumulativeProbability(d1);
            double nd2 = NORMAL.cumulativeProbability(d2);
            
            System.out.println("N(d1)=" + nd1 + ", N(d2)=" + nd2);
            
            double callPrice = S * nd1 - K * Math.exp(-r * t) * nd2;
            
            System.out.println("看涨期权价格计算结果: " + callPrice);
            return BigDecimal.valueOf(callPrice).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            System.out.println("计算看涨期权时出错: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    public static BigDecimal calculatePutPrice(double stockPrice, double strikePrice, double timeToMaturity) {
        System.out.println("OptionPricingUtil - 计算看跌期权: S=" + stockPrice + ", K=" + strikePrice + ", t=" + timeToMaturity);
        
        // 检查参数有效性
        if (stockPrice <= 0 || strikePrice <= 0 || timeToMaturity <= 0) {
            System.out.println("无效的期权参数, 返回零价值");
            return BigDecimal.ZERO;
        }
        
        try {
            double S = stockPrice;
            double K = strikePrice;
            double t = timeToMaturity;
            double r = RISK_FREE_RATE;
            double sigma = VOLATILITY;
            
            double d1 = calculateD1(S, K, t, r, sigma);
            double d2 = d1 - sigma * Math.sqrt(t);
            
            System.out.println("d1=" + d1 + ", d2=" + d2);
            
            double nNegD1 = NORMAL.cumulativeProbability(-d1);
            double nNegD2 = NORMAL.cumulativeProbability(-d2);
            
            System.out.println("N(-d1)=" + nNegD1 + ", N(-d2)=" + nNegD2);
            
            double putPrice = K * Math.exp(-r * t) * nNegD2 - S * nNegD1;
            
            System.out.println("看跌期权价格计算结果: " + putPrice);
            return BigDecimal.valueOf(putPrice).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            System.out.println("计算看跌期权时出错: " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
    
    private static double calculateD1(double S, double K, double t, double r, double sigma) {
        if (t <= 0 || sigma <= 0) {
            throw new IllegalArgumentException("时间和波动率必须为正数");
        }
        return (Math.log(S/K) + (r + sigma * sigma / 2) * t) / (sigma * Math.sqrt(t));
    }
}