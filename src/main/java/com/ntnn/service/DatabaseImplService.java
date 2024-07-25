package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import com.ntnn.entity.History;
import com.ntnn.helper.Iso20022TransactionHelper;
import com.ntnn.entity.Transaction;
import com.ntnn.repository.AsyncTransactionByCustomerRepository;
import com.ntnn.repository.HistoryRepository;
import com.ntnn.repository.TransactionByCustomerRepository;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseImplService {
    private final AsyncTransactionByCustomerRepository asyncTransactionByCustomerRepository;
    private final TransactionByCustomerRepository transactionByCustomerRepository;
    private final HistoryRepository historyRepository;

    public Flux<Transaction> saveTopUpTransactionAsync(Document document, String accountId, StatusType statusType) {
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), accountId, TransactionType.TOPUP.name(), statusType);
        return asyncTransactionByCustomerRepository.saveAll(transactionSet).log().onErrorReturn(null);
    }

    public Iterable<Transaction> saveTopUpTransaction(Document document, String accountId, StatusType statusType) {
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), accountId, TransactionType.TRANSFER.name(), statusType);
        return transactionByCustomerRepository.saveAll(transactionSet).log().toIterable();
    }

    public Flux<History> saveHistoryTransactionAsync(Document document, String accountId, StatusType statusType) {
        Set<History> histories = Iso20022TransactionHelper.populateHistory(document.getFIToFICstmrCdtTrf(), accountId, statusType);
        return historyRepository.saveAll(histories);
    }


}
