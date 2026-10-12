package com.reconplatform.reconciliation_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_balances")
public class LedgerBalanceEntity {

    @Id
    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "expected_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal expectedBalance;

    @Column(name = "last_updated", nullable = false, insertable = false, updatable = false)
    private LocalDateTime lastUpdated;

    public LedgerBalanceEntity() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getExpectedBalance() {
        return expectedBalance;
    }

    public void setExpectedBalance(BigDecimal expectedBalance) {
        this.expectedBalance = expectedBalance;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
