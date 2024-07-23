package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import com.ntnn.entity.TransactionKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface TransactionByCustomerRepository extends CrudRepository<Transaction, TransactionKey> {
    Slice<Transaction> findAllByTransactionKeyAccountIdAndTransactionKeyCreationDateIsBetween(String accountId, Date startDate, Date endDate, Pageable pageable);
}
