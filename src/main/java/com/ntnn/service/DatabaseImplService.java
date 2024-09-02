package com.ntnn.service;

import com.ntnn.common.Currency;
import com.ntnn.common.StatusType;
import com.ntnn.common.TransactionType;
import com.ntnn.common.WalletType;
import com.ntnn.entity.Account;
import com.ntnn.entity.History;
import com.ntnn.exception.InvalidInputException;
import com.ntnn.exception.TechnicalException;
import com.ntnn.helper.Iso20022TransactionHelper;
import com.ntnn.entity.Transaction;
import com.ntnn.repository.AccountByCustomerRepository;
import com.ntnn.repository.AsyncTransactionByCustomerRepository;
import com.ntnn.repository.HistoryRepository;
import com.ntnn.repository.TransactionByCustomerRepository;
import com.ntnn.wsld.Document;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseImplService {
    private final AsyncTransactionByCustomerRepository asyncTransactionByCustomerRepository;
    private final TransactionByCustomerRepository transactionByCustomerRepository;
    private final HistoryRepository historyRepository;
    private final AccountByCustomerRepository accountByCustomerRepository;

    public List<Transaction> saveTopUpTransactionAsync(Document document, String accountId, StatusType statusType) {
        Account account = accountByCustomerRepository.findById(accountId).orElse(accountByCustomerRepository.save(Account.builder().accountId(accountId).creationDate(new Date()).version(1L).currency(Currency.USD).walletType(WalletType.DEBT).build()));
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), account, TransactionType.TOPUP.name(), statusType);
        return asyncTransactionByCustomerRepository.saveAll(transactionSet);
    }

    public List<Transaction> commitPayment(List<Transaction> transactionList) {
        transactionList.forEach(v ->  v.setStatusType(StatusType.COMMIT));
        return asyncTransactionByCustomerRepository.saveAll(transactionList);
    }

    public List<Transaction> saveTopUpTransaction(Document document, Account account, StatusType statusType) {
        Set<Transaction> transactionSet = Iso20022TransactionHelper.populateTransaction(document.getFIToFICstmrCdtTrf(), account, TransactionType.TRANSFER.name(), statusType);
        return transactionByCustomerRepository.saveAll(transactionSet);
    }

    public List<History> saveHistoryTransactionAsync(Document document, Account account, Transaction transaction, StatusType statusType) {
        Set<History> histories = Iso20022TransactionHelper.populateHistory(document.getFIToFICstmrCdtTrf(), account, transaction, statusType);
        return historyRepository.saveAll(histories);
    }


}
