package com.reconplatform.reconciliation_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.reconplatform.common.enums.MismatchReason;

@Entity
@Table(name = "mismatches")
public class MismatchEntity {

    @Id
    @Column(name = "mismatch_id", length = 36)
    private String mismatchId;

    @Column(name = "transaction_id", nullable = false, length = 36)
    private String transactionId;

    @Column(nullable = false, length = 30)
    private String reason;

    @Column(name = "expected_balance", precision = 15, scale = 2)
    private BigDecimal expectedBalance;

    @Column(name = "actual_balance", precision = 15, scale = 2)
    private BigDecimal actualBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal difference;

    @Column(name = "flagged_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime flaggedAt;

    public MismatchEntity() {
    }

    public String getMismatchId() {
        return mismatchId;
    }

    public void setMismatchId(String mismatchId) {
        this.mismatchId = mismatchId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BigDecimal getExpectedBalance() {
        return expectedBalance;
    }

    public void setExpectedBalance(BigDecimal expectedBalance) {
        this.expectedBalance = expectedBalance;
    }

    public BigDecimal getActualBalance() {
        return actualBalance;
    }

    public void setActualBalance(BigDecimal actualBalance) {
        this.actualBalance = actualBalance;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public LocalDateTime getFlaggedAt() {
        return flaggedAt;
    }

    public void setFlaggedAt(LocalDateTime flaggedAt) {
        this.flaggedAt = flaggedAt;
    }
}
