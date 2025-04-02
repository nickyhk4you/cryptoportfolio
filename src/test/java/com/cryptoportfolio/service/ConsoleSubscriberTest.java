package com.cryptoportfolio.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ConsoleSubscriberTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ConsoleSubscriber subscriber;
    
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        subscriber = new ConsoleSubscriber();
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testUpdate() {
        BigDecimal nav = BigDecimal.valueOf(175000.50);
        Map<String, BigDecimal> positionValues = new HashMap<>();
        positionValues.put("AAPL", BigDecimal.valueOf(150000.00));
        positionValues.put("AAPL-OCT-2020-110-C", BigDecimal.valueOf(15000.50));
        
        subscriber.update(nav, positionValues);
        
        String output = outContent.toString();
        assertTrue(output.contains("Market Data Update"));
        assertTrue(output.contains("NAV: 175000.5"));
        assertTrue(output.contains("AAPL"));
    }
    
    @Test
    public void testUpdateWithNullPositionValues() {
        subscriber.update(BigDecimal.ZERO, null);
        
        String output = outContent.toString();
        assertTrue(output.contains("Position values is null!"));
    }
}