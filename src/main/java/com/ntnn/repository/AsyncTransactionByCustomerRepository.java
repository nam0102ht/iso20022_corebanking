package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AsyncTransactionByCustomerRepository extends ReactiveCrudRepository<Transaction, UUID> {
}
