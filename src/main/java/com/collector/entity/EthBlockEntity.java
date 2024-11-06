package com.collector.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(indexName = "ethereum_block")
public class EthBlockEntity implements BaseEntity<String> {

    @Id
    @Field(type = FieldType.Text)
    private String hash;

    @Field(type = FieldType.Text)
    private String number;

    @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date timestamp;

    @Field(type = FieldType.Text)
    private String miner;

    @Field(type = FieldType.Long)
    private String size;

    @Field(type = FieldType.Text)
    private String gasUsed;

    @Field(type = FieldType.Text)
    private String gasLimit;

    @Field(type = FieldType.Text)
    private String parentHash;

    @Field(type = FieldType.Text)
    private String nonce;

    @Field(type = FieldType.Text)
    private String sha3Uncles;

    @Field(type = FieldType.Text)
    private String logsBloom;

    @Field(type = FieldType.Text)
    private String stateRoot;

    @Field(type = FieldType.Text)
    private String transactionsRoot;

    @Field(type = FieldType.Text)
    private String receiptsRoot;

    @Field(type = FieldType.Text)
    private String difficulty;

    @Field(type = FieldType.Text)
    private String totalDifficulty;

    @Field(type = FieldType.Text)
    private String extraData;

    @Field(type = FieldType.Text)
    private String mixHash;

    @Field(type = FieldType.Text)
    private String baseFeePerGas;

    @Field(type = FieldType.Keyword)
    private List<String> transactions;

    @Override
    public String getId() {
        return hash;
    }
}
