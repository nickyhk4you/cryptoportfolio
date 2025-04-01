package com.cryptoportfolio.service;

import com.cryptoportfolio.proto.Position;
import com.cryptoportfolio.proto.PortfolioUpdate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
public class ProtoPortfolioSubscriber implements PortfolioSubscriber {

    @Override
    public void update(BigDecimal nav, Map<String, BigDecimal> positionValues) {
        PortfolioUpdate update = createPortfolioUpdate(nav, positionValues);
        // Here you could send the protobuf message to other systems
    }
    
    private PortfolioUpdate createPortfolioUpdate(BigDecimal nav, Map<String, BigDecimal> positionValues) {
        PortfolioUpdate.Builder builder = PortfolioUpdate.newBuilder()
            .setNav(nav.doubleValue())
            .setTimestamp(Instant.now().toEpochMilli());
            
        positionValues.forEach((ticker, value) ->
            builder.addPositions(Position.newBuilder()
                .setTicker(ticker)
                .setValue(value.doubleValue())
                .build()));
                
        return builder.build();
    }

}