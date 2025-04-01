-- 创建表结构（如果不存在）
CREATE TABLE IF NOT EXISTS securities (
    ticker VARCHAR(50) PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    strike DOUBLE,
    maturity DOUBLE,
    underlying_ticker VARCHAR(50)
);