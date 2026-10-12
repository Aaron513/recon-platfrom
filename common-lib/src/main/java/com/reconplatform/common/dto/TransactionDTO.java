package com.reconplatform.common.dto;

import com.reconplatform.common.enums.MismatchReason;
import com.reconplatform.common.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDTO {

    private String transactionId;
    private String batchId;
    private String accountNumber;
    private BigDecimal amount;
    private String currency;
    private BigDecimal convertedAmount;
    private String targetCurrency;
    private Instant transactionDate;
    private String originIp;        // feeds the geo-risk enrichment call
    private String riskCountry;     // filled in by reconciliation-service
    private TransactionStatus status;  // enum: INGESTED, ENRICHED, FLAGGED, RECONCILED
    private String errorCode;       // nullable
    private MismatchReason flagReason;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getOriginIp() {
        return originIp;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    public String getRiskCountry() {
        return riskCountry;
    }

    public void setRiskCountry(String riskCountry) {
        this.riskCountry = riskCountry;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public MismatchReason getFlagReason() {
        return flagReason;
    }

    public void setFlagReason(MismatchReason flagReason) {
        this.flagReason = flagReason;
    }

}
