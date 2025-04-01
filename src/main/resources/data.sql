-- Stocks
INSERT INTO securities (ticker, type) VALUES ('AAPL', 'STOCK');
INSERT INTO securities (ticker, type) VALUES ('TELSA', 'STOCK');

-- Call Options
INSERT INTO securities (ticker, type, strike, maturity, underlying_ticker) VALUES 
    ('AAPL-OCT-2020-110-C', 'CALL', 110.0, 0.25, 'AAPL'),
    ('TELSA-NOV-2020-400-C', 'CALL', 400.0, 0.33, 'TELSA');

-- Put Options
INSERT INTO securities (ticker, type, strike, maturity, underlying_ticker) VALUES 
    ('AAPL-OCT-2020-110-P', 'PUT', 110.0, 0.25, 'AAPL'),
    ('TELSA-DEC-2020-400-P', 'PUT', 400.0, 0.5, 'TELSA');