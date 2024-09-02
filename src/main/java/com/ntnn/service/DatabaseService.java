package com.ntnn.service;

import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import com.ntnn.entity.Account;
import com.ntnn.entity.History;
import com.ntnn.helper.Iso20022TransactionHelper;
import com.ntnn.entity.Transaction;
import com.ntnn.repository.AccountByCustomerRepository;
import com.ntnn.repository.AsyncTransactionByCustomerRepository;
import com.ntnn.repository.HistoryRepository;
import com.ntnn.repository.TransactionByCustomerRepository;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabaseService {
    private final AsyncTransactionByCustomerRepository asyncTransactionByCustomerRepository;
    private final TransactionByCustomerRepository transactionByCustomerRepository;
    private final HistoryRepository historyRepository;
    private final AccountByCustomerRepository accountByCustomerRepository;

    public List<Transaction> saveTopUpTransactionAsync(Document document, String accountId, StatusType statusType) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).build()));
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), account, TransactionType.TOPUP.name(), statusType);
        return asyncTransactionByCustomerRepository.saveAll(transactionSet);
    }

    public List<Transaction> saveTopUpTransaction(Document document, String accountId, StatusType statusType) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).build()));
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), account, TransactionType.TRANSFER.name(), statusType);
        return transactionByCustomerRepository.saveAll(transactionSet);
    }

    @Cacheable(value = "getTransactionHistoryByCustomer")
    public Slice<Transaction> getTransactionHistoryByCustomer(String transactionId, String accountId, Date startDate, Date endDate, Pageable pageable) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).build()));
        return transactionByCustomerRepository.findAllByTransactionIdAccountIdAndTransactionKeyCreationDateIsBetween(transactionId, accountId, startDate, endDate, pageable);
    }

    public List<History> saveHistoryTransactionAsync(Document document, String accountId, Transaction transaction, StatusType statusType) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).build()));
        Set<History> histories = Iso20022TransactionHelper.populateHistory(document.getFIToFICstmrCdtTrf(), account, transaction, statusType);
        return historyRepository.saveAll(histories);
    }


}
