package com.reconplatform.common.enums;

public enum CommandType {
    REPROCESS_BATCH("reprocess_batch", "Reprocess failed batch of messages"),
    MANUAL_REVIEW_RESOLVED("manual_review_resolved", "Manual review resolved");

    private final String code;
    private final String message;

    CommandType(String code, String message) {
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
