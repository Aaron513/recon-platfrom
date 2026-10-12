CREATE TABLE transactions (
    transaction_id    VARCHAR(36)   PRIMARY KEY,
    batch_id          VARCHAR(36)   NOT NULL,
    account_number    VARCHAR(20)   NOT NULL,
    amount            DECIMAL(15,2) NOT NULL,
    currency          VARCHAR(3)    NOT NULL,
    converted_amount  DECIMAL(15,2),
    target_currency   VARCHAR(3),
    transaction_date  TIMESTAMP     NOT NULL,
    origin_ip         VARCHAR(45),
    risk_country      VARCHAR(2),
    status            VARCHAR(20)   NOT NULL,
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ledger_balances (
    account_number    VARCHAR(20)   PRIMARY KEY,
    expected_balance  DECIMAL(15,2) NOT NULL,
    last_updated      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE mismatches (
    mismatch_id       VARCHAR(36)   PRIMARY KEY,
    transaction_id    VARCHAR(36)   NOT NULL,
    reason            VARCHAR(30)   NOT NULL,
    expected_balance  DECIMAL(15,2),
    actual_balance    DECIMAL(15,2),
    difference        DECIMAL(15,2),
    flagged_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mismatch_txn FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id)
);

CREATE TABLE audit_log (
    audit_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id    VARCHAR(36)   NOT NULL,
    action            VARCHAR(50)   NOT NULL,
    performed_by      VARCHAR(50)   NOT NULL DEFAULT 'SYSTEM',
    performed_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    details           VARCHAR(500)
);