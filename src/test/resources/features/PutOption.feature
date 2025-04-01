Feature: Put Option Price Calculation
  As a trader
  I want to calculate the price of put options
  So that I can value my portfolio correctly

  Scenario: Calculate put option price when underlying price is less than strike price
    Given a put option with ticker "AAPL-P" with strike price 120.0 and maturity 0.5
    When the underlying price is 100.0
    Then the option price should be 20.0

  Scenario: Calculate put option price when underlying price is greater than strike price
    Given a put option with ticker "AAPL-P" with strike price 100.0 and maturity 0.5
    When the underlying price is 120.0
    Then the option price should be 0.0

  Scenario: Creating a put option with invalid strike price
    When I try to create a put option with ticker "AAPL-P" with strike price -10.0 and maturity 0.5
    Then an IllegalArgumentException should be thrown with message "Strike price must be positive"

  Scenario: Creating a put option with invalid maturity
    When I try to create a put option with ticker "AAPL-P" with strike price 100.0 and maturity -0.5
    Then an IllegalArgumentException should be thrown with message "Maturity must be positive"

  Scenario: Calculate put option price with null underlying price
    Given a put option with ticker "AAPL-P" with strike price 100.0 and maturity 0.5
    When I try to calculate price with null underlying price
    Then a NullPointerException should be thrown with message "Underlying price cannot be null"