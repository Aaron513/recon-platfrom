package com.reconplatform.common.enums;

public enum ReviewStatus {
    PENDING("pending", "Transaction is pending review"),
    APPROVED("approved", "Transaction has been approved"),
    REJECTED("rejected", "Transaction has been rejected");

    private final String code;
    private final String message;

    ReviewStatus(String code, String message) {
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
