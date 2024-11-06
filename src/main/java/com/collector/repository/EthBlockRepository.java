package com.collector.repository;

import com.collector.entity.EthBlockEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface EthBlockRepository extends ElasticsearchRepository<EthBlockEntity, String> {
    Optional<EthBlockEntity> findTopByOrderByTimestampDesc();
}
