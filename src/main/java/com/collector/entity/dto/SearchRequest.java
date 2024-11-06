package com.collector.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    @NonNull
    private String fieldName;
    @NonNull
    private String fieldValue;
    @NonNull
    private Operation operation;

    public SearchRequest() {
    }

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        EQUALS("="),
        NOT_EQUALS("!="),
        CONTAINS("%");

        private final String operation;

        @JsonCreator
        public static Operation fromValue(String value) {
            for (Operation op : Operation.values()) {
                if (op.operation.equals(value)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("Unknown enum value: " + value);
        }
    }
}
