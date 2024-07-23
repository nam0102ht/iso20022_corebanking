package com.ntnn.repository;

import com.ntnn.entity.Account;
import com.ntnn.entity.AccountKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AccountByCustomerRepository extends ReactiveCrudRepository<Account, AccountKey> {
}
