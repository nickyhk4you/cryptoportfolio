-- Stocks
INSERT INTO securities (ticker, type) VALUES ('AAPL', 'STOCK');
INSERT INTO securities (ticker, type) VALUES ('TELSA', 'STOCK');

-- Call Options
INSERT INTO securities (ticker, type, strike, maturity) VALUES 
    ('AAPL_CALL_150', 'CALL', 150.0, 20231215),
    ('AAPL-OCT-2020-110-C', 'CALL', 110.0, 20201031),
    ('TELSA-NOV-2020-400-C', 'CALL', 400.0, 20201130);

-- Put Options
INSERT INTO securities (ticker, type, strike, maturity) VALUES 
    ('AAPL-OCT-2020-110-P', 'PUT', 110.0, 20201031),
    ('TELSA-DEC-2020-400-P', 'PUT', 400.0, 20201231);