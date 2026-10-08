package com.reconplatform.ingestion_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "raw_batch")
public class RawBatchEntity {

    @Id
    @Column(length = 36)
    private String batchId;

    @Column(nullable = false, length = 100)
    private String sourceName;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false)
    private int transactionCount;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(length = 20)
    private String errorCode;

    @Column(name = "received_at", insertable = false, updatable = false)
    private LocalDateTime receivedAt;

    public RawBatchEntity() {}

    public RawBatchEntity(String batchId, String sourceName, String fileName, int transactionCount, String status) {
        this.batchId = batchId;
        this.sourceName = sourceName;
        this.fileName = fileName;
        this.transactionCount = transactionCount;
        this.status = status;
    }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public int getTransactionCount() { return transactionCount; }
    public void setTransactionCount(int transactionCount) { this.transactionCount = transactionCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
}