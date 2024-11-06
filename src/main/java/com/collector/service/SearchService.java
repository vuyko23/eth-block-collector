package com.collector.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.collector.entity.BaseEntity;
import com.collector.entity.dto.SearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SearchService {
    private final ElasticsearchOperations elasticsearchOperations;

    public <T extends BaseEntity<String>> List<T> searchBlocks(List<SearchRequest> params, Class<T> clazz) {
        BoolQuery.Builder bool = QueryBuilders.bool();

        for (SearchRequest param : params) {
            switch (param.getOperation()) {
                case EQUALS:
                    bool.must(QueryBuilders.term((s) -> s.field(param.getFieldName()).value(param.getFieldValue())));
                    break;
                case NOT_EQUALS:
                     bool.mustNot(QueryBuilders.term((s) -> s.field(param.getFieldName()).value(param.getFieldValue())));
                     break;
                case CONTAINS:
                    bool.must(QueryBuilders.wildcard((s) -> s.field(param.getFieldName()).value("*" + param.getFieldValue() + "*")));
                    break;
            }
        }

        NativeQuery query = new NativeQueryBuilder()
                .withFilter(bool.build()._toQuery())
                .build();

        return elasticsearchOperations.search(query, clazz)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
