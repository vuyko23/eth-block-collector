package com.collector.mapper;

import com.collector.entity.EthBlockEntity;
import com.collector.entity.EthTransaction;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EthBlockDtoToEthBlockEntity {
    public static EthBlockEntity convert(EthBlock ethBlock, List<EthTransaction> transactions) {
        EthBlock.Block block = ethBlock.getBlock();

        EthBlockEntity ethBlockEntity = new EthBlockEntity();
        ethBlockEntity.setHash(block.getHash());
        ethBlockEntity.setNumber(block.getNumber().toString());
        ethBlockEntity.setTimestamp(new Date(block.getTimestamp().longValue()));
        ethBlockEntity.setMiner(block.getMiner());
        ethBlockEntity.setSize(block.getSize().toString());
        ethBlockEntity.setGasUsed(block.getGasUsed().toString());
        ethBlockEntity.setGasLimit(block.getGasLimit().toString());
        ethBlockEntity.setParentHash(block.getParentHash());
        ethBlockEntity.setNonce(block.getNonceRaw());
        ethBlockEntity.setSha3Uncles(block.getSha3Uncles());
        ethBlockEntity.setLogsBloom(block.getLogsBloom());
        ethBlockEntity.setStateRoot(block.getStateRoot());
        ethBlockEntity.setTransactionsRoot(block.getTransactionsRoot());
        ethBlockEntity.setReceiptsRoot(block.getReceiptsRoot());
        ethBlockEntity.setDifficulty(block.getDifficulty().toString());
        ethBlockEntity.setTotalDifficulty(block.getTotalDifficulty().toString());
        ethBlockEntity.setExtraData(block.getExtraData());
        ethBlockEntity.setMixHash(block.getMixHash());
        ethBlockEntity.setBaseFeePerGas(block.getBaseFeePerGas().toString());

        ethBlockEntity.setTransactions(transactions.stream().map(EthTransaction::getHash).collect(Collectors.toList()));

        return ethBlockEntity;
    }

    public static List<EthTransaction> convertTransaction(EthBlock ethBlock) {
        return Optional.ofNullable(ethBlock).map(b -> b.getBlock().getTransactions().stream()
                .map(tx -> {
                    EthBlock.TransactionObject transaction = (EthBlock.TransactionObject) tx.get();
                    EthTransaction ethTransaction = new EthTransaction();
                    ethTransaction.setHash(transaction.getHash());
                    ethTransaction.setFrom(transaction.getFrom());
                    ethTransaction.setTo(transaction.getTo());
                    ethTransaction.setValue(transaction.getValue().toString());
                    ethTransaction.setGas(transaction.getGas().toString());
                    ethTransaction.setGasPrice(transaction.getGasPrice().toString());
                    ethTransaction.setInput(transaction.getInput());
                    return ethTransaction;
                })
                .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
}
