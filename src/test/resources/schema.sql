-- Create securities table
CREATE TABLE IF NOT EXISTS securities (
    ticker VARCHAR(50) PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    strike DOUBLE,
    maturity DOUBLE,
    underlying_ticker VARCHAR(50)
);

-- Create positions table
CREATE TABLE IF NOT EXISTS positions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticker VARCHAR(50) NOT NULL,
    quantity DECIMAL(20,8) NOT NULL,
    FOREIGN KEY (ticker) REFERENCES securities(ticker)
);