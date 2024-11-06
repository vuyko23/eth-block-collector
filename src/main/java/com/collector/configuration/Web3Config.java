package com.collector.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3Config {

    @Bean
    public Web3j web3j(@Value("${pool.url}") String poolUrl) {
        return Web3j.build(new HttpService(poolUrl));
    }
}
