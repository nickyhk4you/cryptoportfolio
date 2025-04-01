package com.cryptoportfolio.steps;

import com.cryptoportfolio.model.PutOption;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PutOptionSteps {
    private PutOption putOption;
    private BigDecimal calculatedPrice;
    private Exception thrownException;

    @Given("a put option with ticker {string} with strike price {double} and maturity {double}")
    public void aPutOptionWithTickerWithStrikePriceAndMaturity(String ticker, double strike, double maturity) {
        // Adding "AAPL" as the underlying ticker
        putOption = new PutOption(ticker, strike, maturity, "AAPL");
    }

    @When("the underlying price is {double}")
    public void theUnderlyingPriceIs(double price) {
        calculatedPrice = putOption.calculatePrice(BigDecimal.valueOf(price));
    }

    @Then("the option price should be greater than {double}")
    public void theOptionPriceShouldBeGreaterThan(double expectedMinPrice) {
        assertTrue("Option price should be greater than " + expectedMinPrice,
            calculatedPrice.compareTo(BigDecimal.valueOf(expectedMinPrice)) > 0);
    }
    
    @Then("the option price should be less than {double}")
    public void theOptionPriceShouldBeLessThan(double expectedMaxPrice) {
        assertTrue("Option price should be less than " + expectedMaxPrice,
            calculatedPrice.compareTo(BigDecimal.valueOf(expectedMaxPrice)) < 0);
    }

    @When("I try to create a put option with ticker {string} with strike price {double} and maturity {double}")
    public void iTryToCreateAPutOptionWithInvalidParameters(String ticker, double strike, double maturity) {
        try {
            putOption = new PutOption(ticker, strike, maturity, "AAPL");
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @When("I try to calculate price with null underlying price")
    public void iTryToCalculatePriceWithNullUnderlyingPrice() {
        try {
            calculatedPrice = putOption.calculatePrice(null);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("an IllegalArgumentException should be thrown with message {string}")
    public void anIllegalArgumentExceptionShouldBeThrownWithMessage(String message) {
        if (!(thrownException instanceof IllegalArgumentException)) {
            fail("Expected IllegalArgumentException but got " + 
                 (thrownException == null ? "no exception" : thrownException.getClass().getSimpleName()));
        }
        assertEquals(message, thrownException.getMessage());
    }

    @Then("a NullPointerException should be thrown with message {string}")
    public void aNullPointerExceptionShouldBeThrownWithMessage(String message) {
        if (!(thrownException instanceof NullPointerException)) {
            fail("Expected NullPointerException but got " + 
                 (thrownException == null ? "no exception" : thrownException.getClass().getSimpleName()));
        }
        assertEquals(message, thrownException.getMessage());
    }
}