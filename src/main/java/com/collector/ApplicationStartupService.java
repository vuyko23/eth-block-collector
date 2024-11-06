package com.collector;

import com.collector.service.PoolDataExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ApplicationStartupService implements ApplicationListener<ApplicationReadyEvent> {
    private final PoolDataExecutorService poolDataExecutorService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application started, started pool data executor service");
        poolDataExecutorService.start();
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
