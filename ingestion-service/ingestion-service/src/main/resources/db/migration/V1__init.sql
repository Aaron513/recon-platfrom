CREATE TABLE raw_batch (
    batch_id           VARCHAR(36)  PRIMARY KEY,
    source_name        VARCHAR(100) NOT NULL,
    file_name          VARCHAR(255) NOT NULL,
    received_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_count  INT          NOT NULL,
    status             VARCHAR(20)  NOT NULL,
    error_code         VARCHAR(20)
);