package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionByCustomerRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT e from transaction e " +
        "WHERE e.id = :transactionId " +
        "AND e.account.accountId = :accountId " +
        "AND e.creationDate BETWEEN :startDate AND :endDate")
    Slice<Transaction> findAllByTransactionIdAccountIdAndTransactionKeyCreationDateIsBetween(String transactionId, String accountId, Date startDate, Date endDate, Pageable pageable);
}
