package com.collector.service;

import com.collector.configuration.DynamicScheduler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Service
public class PoolDataExecutorService {
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final DynamicScheduler dynamicScheduler;
    private final PoolDataService poolDataService;
    private final Integer poolRate;

    public PoolDataExecutorService(DynamicScheduler dynamicScheduler,
                                   PoolDataService poolDataService,
                                   @Value("${pool.rate}") Integer poolRate) {
        this.dynamicScheduler = dynamicScheduler;
        this.poolDataService = poolDataService;
        this.poolRate = poolRate;

        if (poolRate == null || poolRate < 1) {
            throw new IllegalArgumentException("Pool rate must be greater than 0");
        }
    }

    public void start() {
        if (isRunning.get()) {
            log.warn("Pool data executor service is already running");
            return;
        }

        dynamicScheduler.start(poolDataService::collectData, poolRate);
        isRunning.set(true);
        log.info("Pool data executor service started");
    }

    public void stop() {
        dynamicScheduler.stop();
        isRunning.set(false);
        log.info("Pool data executor service stopped");
    }
}
