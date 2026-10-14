CREATE TABLE flagged_reviews (
    review_id          VARCHAR(36)   PRIMARY KEY,
    transaction_id     VARCHAR(36)   NOT NULL,
    account_number     VARCHAR(20)   NOT NULL,
    amount             DECIMAL(15,2) NOT NULL,
    currency           VARCHAR(3)    NOT NULL,
    converted_amount   DECIMAL(15,2),
    mismatch_reason    VARCHAR(255)  NOT NULL,
    status             VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    reviewed_by        VARCHAR(50),
    reviewed_at        TIMESTAMP     NULL,
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE app_user (
    user_id            VARCHAR(36)  PRIMARY KEY,
    username           VARCHAR(50)  UNIQUE NOT NULL,
    password_hash      VARCHAR(255) NOT NULL,
    role               VARCHAR(20)  NOT NULL DEFAULT 'REVIEWER'
);