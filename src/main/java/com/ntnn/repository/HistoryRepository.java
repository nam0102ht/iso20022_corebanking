package com.ntnn.repository;

import com.ntnn.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface HistoryRepository extends JpaRepository<History, String> {
}
