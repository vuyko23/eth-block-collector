package com.collector.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "ethereum_transaction")
public class EthTransaction implements BaseEntity<String> {

    @Id
    @Field(type = FieldType.Text)
    private String hash;

    @Field(type = FieldType.Text)
    private String from;

    @Field(type = FieldType.Text)
    private String to;

    @Field(type = FieldType.Text)
    private String value;

    @Field(type = FieldType.Text)
    private String gas;

    @Field(type = FieldType.Text)
    private String gasPrice;

    @Field(type = FieldType.Text)
    private String input;

    @Override
    public String getId() {
        return hash;
    }
}
