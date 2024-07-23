package com.ntnn.repository;

import com.ntnn.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountByCustomerRepository extends ReactiveCrudRepository<Account, UUID> {
}
