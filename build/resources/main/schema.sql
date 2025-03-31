CREATE TABLE IF NOT EXISTS securities (
    ticker VARCHAR(20) PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    strike DOUBLE,
    maturity DOUBLE
);