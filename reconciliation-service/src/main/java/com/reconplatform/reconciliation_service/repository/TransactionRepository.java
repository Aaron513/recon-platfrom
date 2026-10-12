package com.reconplatform.reconciliation_service.repository;

import com.reconplatform.reconciliation_service.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    boolean existsById(String transactionId);
}
