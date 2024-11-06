package com.collector.rest;

import com.collector.entity.EthBlockEntity;
import com.collector.entity.EthTransaction;
import com.collector.entity.dto.SearchRequest;
import com.collector.service.PoolDataExecutorService;
import com.collector.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiController {
    private final PoolDataExecutorService poolDataExecutorService;
    private final SearchService searchService;

    @PostMapping("/pool/start")
    public String startPool() {
        poolDataExecutorService.start();

        return "Started to collect Ethereum block data";
    }

    @DeleteMapping("/pool/stop")
    public String stopPool() {
        poolDataExecutorService.stop();

        return "Stopped collecting Ethereum block data";
    }


    @PostMapping("/blocks/search")
    public List<EthBlockEntity> searchBlocks(@RequestBody List<SearchRequest> params) {
        return searchService.searchBlocks(params, EthBlockEntity.class);
    }

    @PostMapping("/transaction/search")
    public List<EthTransaction> searchTransactions(@RequestBody List<SearchRequest> params) {
        return searchService.searchBlocks(params, EthTransaction.class);
    }
}
