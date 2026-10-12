package com.reconplatform.reconciliation_service.repository;

import com.reconplatform.reconciliation_service.model.LedgerBalanceEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerBalanceRepository extends JpaRepository<LedgerBalanceEntity, String> {
    Optional<LedgerBalanceEntity> findByAccountNumber(String accountNumber);
}
