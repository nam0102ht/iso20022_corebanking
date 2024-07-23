package com.ntnn.repository;

import com.ntnn.entity.History;
import com.ntnn.entity.HistoryKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface HistoryRepository extends ReactiveCrudRepository<History, HistoryKey> {
}
