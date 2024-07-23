package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import com.ntnn.entity.TransactionKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AsyncTransactionByCustomerRepository extends ReactiveCrudRepository<Transaction, TransactionKey> {
}
