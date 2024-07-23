package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.UUID;

@Repository
public interface TransactionByCustomerRepository extends ReactiveCrudRepository<Transaction, UUID> {

    @Query("SELECT e from transaction e WHERE e.transactionId = :transactionId AND e.accountId = : accountId AND e.creationDate BETWEEN :startDate AND :endDate")
    Flux<Transaction> findAllByTransactionIdAccountIdAndTransactionKeyCreationDateIsBetween(String transactionId, String accountId, Date startDate, Date endDate, Pageable pageable);
}
