package com.collector.service;

import com.collector.entity.EthBlockEntity;
import com.collector.entity.EthTransaction;
import com.collector.mapper.EthBlockDtoToEthBlockEntity;
import com.collector.repository.EthBlockRepository;
import com.collector.repository.EthTransactionRepository;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.math.BigInteger;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
@Service
@Transactional
public class PoolDataService {
    private final AtomicReference<BigInteger> lastBlockNumber = new AtomicReference<>(BigInteger.ZERO);
    private final Queue<EthBlockEntity> blockBuffer = new ConcurrentLinkedDeque<>();
    private final Queue<EthTransaction> transactionBuffer = new ConcurrentLinkedDeque<>();
    private final EthTransactionRepository transactionRepository;
    private final EthBlockRepository ethBlockRepository;
    private final Integer blockBatchSize;
    private final Web3j web3j;

    public PoolDataService(EthTransactionRepository transactionRepository,
                           EthBlockRepository repository,
                           @Value("${block.batch.size}") Integer batchSize,
                           Web3j web3j) {
        this.transactionRepository = transactionRepository;
        this.ethBlockRepository = repository;
        this.blockBatchSize = batchSize;
        this.web3j = web3j;


        if (batchSize == null || batchSize < 1) {
            throw new IllegalArgumentException("Batch size must be greater than 0");
        }
    }

    public void collectData() {
        try {
            if (lastBlockNumber.get().equals(BigInteger.ZERO)) {
                log.info("First start, trying to find last block number that was proceeded");
                ethBlockRepository.findTopByOrderByTimestampDesc().ifPresent(block -> lastBlockNumber.set(
                        new BigInteger(block.getNumber()).add(BigInteger.ONE)));
            }

            if (lastBlockNumber.get().equals(BigInteger.ZERO)) {
                log.info("No blocks found in database, trying to find last block number from the network");
                lastBlockNumber.set(web3j.ethBlockNumber().send().getBlockNumber());
            }

            while (true) {
                log.info("Trying to find block - {}", lastBlockNumber.get().toString());
                EthBlock latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(lastBlockNumber.get()), true).send();
                if (latestBlock.getBlock() == null) {
                    log.info("Block not found, stopping");
                    break;
                }

                List<EthTransaction> ethTransactions = EthBlockDtoToEthBlockEntity.convertTransaction(latestBlock);
                EthBlockEntity entity = EthBlockDtoToEthBlockEntity.convert(latestBlock, ethTransactions);

                addToBuffer(entity, ethTransactions);
                lastBlockNumber.set(lastBlockNumber.get().add(BigInteger.ONE));
            }

        } catch (Exception e) {
            log.error("Error when trying to find block - {}", lastBlockNumber.get().toString(), e);
        }
    }

    @PreDestroy
    public void beforeDestroy() {
        log.info("Saving buffered values before shutdown");
        flushBuffer();
    }

    private void addToBuffer(EthBlockEntity ethBlockEntity, List<EthTransaction> transactions) {
        log.info("Adding block to buffer: {}", ethBlockEntity.getNumber());
        blockBuffer.add(ethBlockEntity);
        transactionBuffer.addAll(transactions);

        if (blockBuffer.size() > blockBatchSize) {
            flushBuffer();
        }
    }

    private void flushBuffer() {
        log.info("Flushing buffer with {} blocks and transaction  {}", blockBuffer.size(), transactionBuffer.size());
        transactionRepository.saveAll(transactionBuffer);
        ethBlockRepository.saveAll(blockBuffer);
        blockBuffer.clear();
        transactionBuffer.clear();
        log.info("Successfully flushed buffer");
    }
}
