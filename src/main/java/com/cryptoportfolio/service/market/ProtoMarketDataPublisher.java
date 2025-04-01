package com.cryptoportfolio.service.market;

import com.cryptoportfolio.proto.MarketData;
import com.cryptoportfolio.proto.SecurityPrice;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProtoMarketDataPublisher extends MarketDataPublisher {
    
    public MarketData getMarketDataProto() {
        Map<String, BigDecimal> prices = getAllPrices();
        
        MarketData.Builder builder = MarketData.newBuilder()
            .setTimestamp(Instant.now().toEpochMilli());
            
        prices.forEach((ticker, price) -> 
            builder.addPrices(SecurityPrice.newBuilder()
                .setTicker(ticker)
                .setPrice(price.doubleValue())
                .build()));
                
        return builder.build();
    }
    
    public Map<String, BigDecimal> fromProto(MarketData marketData) {
        return marketData.getPricesList().stream()
            .collect(Collectors.toMap(
                SecurityPrice::getTicker,
                price -> BigDecimal.valueOf(price.getPrice())));
    }
}