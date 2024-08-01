package com.ntnn.repository;

import com.ntnn.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountByCustomerRepository extends JpaRepository<Account, String> {
}
