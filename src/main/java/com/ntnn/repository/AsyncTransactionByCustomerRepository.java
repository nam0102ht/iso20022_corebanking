package com.ntnn.repository;

import com.ntnn.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AsyncTransactionByCustomerRepository extends JpaRepository<Transaction, String> {
}
