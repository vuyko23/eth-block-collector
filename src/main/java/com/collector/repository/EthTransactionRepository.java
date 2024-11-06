package com.collector.repository;

import com.collector.entity.EthTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EthTransactionRepository extends ElasticsearchRepository<EthTransaction, String> {
}
