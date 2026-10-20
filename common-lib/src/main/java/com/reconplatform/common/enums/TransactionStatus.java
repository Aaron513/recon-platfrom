package com.reconplatform.common.enums;

public enum TransactionStatus {
    INGESTED("ingested", "Transaction has been ingested into the system"),
    ENRICHED("enriched", "Transaction has been enriched"),
    FLAGGED("flagged", "Transaction has been flagged"),
    RECONCILED("reconciled", "Transaction has been reconciled"),
    REJECTED("rejected", "Transaction has been rejected");

    private final String code;
    private final String message;

    TransactionStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
