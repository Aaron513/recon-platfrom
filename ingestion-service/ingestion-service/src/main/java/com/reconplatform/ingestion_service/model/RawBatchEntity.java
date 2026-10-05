package com.reconplatform.ingestion_service.model;

@Entity
@Table(name = "raw_batch")
public class RawBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batch_id;


    @Column(nullable = false, length=100)
    private String source_name;

    @Column(nullable = false, length=255)
    private String file_name;

    @Column(nullable = false)
    private int transaction_count;

    @Column(nullable = false, length=20)
    private String status;

    private String error_code;

    @Column(name = "received_at", insertable = false, updatable = false)
    @Generated(event = EventType.INSERT)
    private LocalDateTime received_at;

    public RawBatchEntity(Long batch_id, String source_name, String file_name, int transaction_count, String status, String error_code, LocalDateTime received_at) {
        this.batch_id = batch_id;
        this.source_name = source_name;
        this.file_name = file_name;
        this.transaction_count = transaction_count;
        this.status = status;
        this.error_code = error_code;
        this.received_at = received_at;
    }

    public RawBatchEntity() {
    }

    public Long getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(Long batch_id) {
        this.batch_id = batch_id;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getTransaction_count() {
        return transaction_count;
    }

    public void setTransaction_count(int transaction_count) {
        this.transaction_count = transaction_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public LocalDateTime getReceived_at() {
        return received_at;
    }

    public void setReceived_at(LocalDateTime received_at) {
        this.received_at = received_at;
    }
}