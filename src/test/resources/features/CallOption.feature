Feature: Call Option Price Calculation
  As a trader
  I want to calculate the price of call options
  So that I can value my portfolio correctly

  Scenario: Calculate call option price when underlying price is greater than strike price
    Given a call option with ticker "AAPL-C" with strike price 100.0 and maturity 0.5
    When the underlying price for call is 120.0
    Then the call option price should be greater than 15.0

  Scenario: Calculate call option price when underlying price is less than strike price
    Given a call option with ticker "AAPL-C" with strike price 120.0 and maturity 0.5
    When the underlying price for call is 100.0
    Then the call option price should be less than 5.0

  Scenario: Creating a call option with invalid strike price
    When I try to create a call option with ticker "AAPL-C" with strike price -10.0 and maturity 0.5
    Then an IllegalArgumentException for call should be thrown with message "Strike price must be positive"

  Scenario: Creating a call option with invalid maturity
    When I try to create a call option with ticker "AAPL-C" with strike price 100.0 and maturity -0.5
    Then an IllegalArgumentException for call should be thrown with message "Maturity must be positive"

  Scenario: Calculate call option price with null underlying price
    Given a call option with ticker "AAPL-C" with strike price 100.0 and maturity 0.5
    When I try to calculate call price with null underlying price
    Then a NullPointerException for call should be thrown with message "Underlying price cannot be null"