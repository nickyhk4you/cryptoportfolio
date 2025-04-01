package com.cryptoportfolio.steps;

import com.cryptoportfolio.model.CallOption;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CallOptionSteps {
    private CallOption callOption;
    private BigDecimal calculatedPrice;
    private Exception thrownException;

    @Given("a call option with ticker {string} with strike price {double} and maturity {double}")
    public void aCallOptionWithTickerWithStrikePriceAndMaturity(String ticker, double strike, double maturity) {
        callOption = new CallOption(ticker, strike, maturity, "AAPL");
    }

    @When("the underlying price for call is {double}")
    public void theUnderlyingPriceForCallIs(double price) {
        calculatedPrice = callOption.calculatePrice(BigDecimal.valueOf(price));
    }

    @Then("the call option price should be greater than {double}")
    public void theCallOptionPriceShouldBeGreaterThan(double expectedMinPrice) {
        assertTrue("Call option price should be greater than " + expectedMinPrice,
            calculatedPrice.compareTo(BigDecimal.valueOf(expectedMinPrice)) > 0);
    }
    
    @Then("the call option price should be less than {double}")
    public void theCallOptionPriceShouldBeLessThan(double expectedMaxPrice) {
        assertTrue("Call option price should be less than " + expectedMaxPrice,
            calculatedPrice.compareTo(BigDecimal.valueOf(expectedMaxPrice)) < 0);
    }

    @When("I try to create a call option with ticker {string} with strike price {double} and maturity {double}")
    public void iTryToCreateACallOptionWithInvalidParameters(String ticker, double strike, double maturity) {
        try {
            callOption = new CallOption(ticker, strike, maturity, "AAPL");
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @When("I try to calculate call price with null underlying price")
    public void iTryToCalculateCallPriceWithNullUnderlyingPrice() {
        try {
            calculatedPrice = callOption.calculatePrice(null);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an IllegalArgumentException for call should be thrown with message {string}")
    public void anIllegalArgumentExceptionForCallShouldBeThrownWithMessage(String message) {
        if (!(thrownException instanceof IllegalArgumentException)) {
            fail("Expected IllegalArgumentException but got " + 
                 (thrownException == null ? "no exception" : thrownException.getClass().getSimpleName()));
        }
        assertEquals(message, thrownException.getMessage());
    }

    @Then("a NullPointerException for call should be thrown with message {string}")
    public void aNullPointerExceptionForCallShouldBeThrownWithMessage(String message) {
        if (!(thrownException instanceof NullPointerException)) {
            fail("Expected NullPointerException but got " + 
                 (thrownException == null ? "no exception" : thrownException.getClass().getSimpleName()));
        }
        assertEquals(message, thrownException.getMessage());
    }
}