package com.cryptoportfolio.steps;

import com.cryptoportfolio.model.PutOption;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PutOptionSteps {
    private PutOption putOption;
    private BigDecimal calculatedPrice;
    private Exception thrownException;

    @Given("a put option with ticker {string} with strike price {double} and maturity {double}")
    public void aPutOptionWithTickerWithStrikePriceAndMaturity(String ticker, double strike, double maturity) {
        putOption = new PutOption(ticker, strike, maturity);
    }

    @When("the underlying price is {double}")
    public void theUnderlyingPriceIs(double price) {
        calculatedPrice = putOption.calculatePrice(BigDecimal.valueOf(price));
    }

    @Then("the option price should be {double}")
    public void theOptionPriceShouldBe(double expectedPrice) {
        assertEquals(BigDecimal.valueOf(expectedPrice).setScale(2), calculatedPrice);
    }

    @When("I try to create a put option with ticker {string} with strike price {double} and maturity {double}")
    public void iTryToCreateAPutOptionWithInvalidParameters(String ticker, double strike, double maturity) {
        try {
            putOption = new PutOption(ticker, strike, maturity);
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