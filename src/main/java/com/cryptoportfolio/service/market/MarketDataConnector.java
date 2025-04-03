package com.cryptoportfolio.service.market;

import com.cryptoportfolio.service.MarketDataService;

public class MarketDataConnector {
    private final MarketDataPublisher publisher;
    private final MarketDataService service;
    private Thread connectorThread;
    private volatile boolean running = true;

    public MarketDataConnector(MarketDataPublisher publisher, MarketDataService service) {
        this.publisher = publisher;
        this.service = service;
    }

    public void start() {
        connectorThread = new Thread(() -> {
            while (running) {
                try {
                    // 获取最新市场数据并处理
                    service.processMarketDataUpdate(publisher.getAllPrices());
                    Thread.sleep(1000); // 每秒更新一次
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("市场数据处理错误: " + e.getMessage());
                }
            }
        });
        connectorThread.setName("market-data-connector");
        connectorThread.setDaemon(true);
        connectorThread.start();
    }

    public void stop() {
        running = false;
        if (connectorThread != null) {
            connectorThread.interrupt();
        }
    }
}
