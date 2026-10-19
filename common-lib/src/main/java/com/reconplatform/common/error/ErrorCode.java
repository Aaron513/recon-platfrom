package com.reconplatform.common.error;

public enum ErrorCode {
    INGESTION_XML_INVALID("ING-001", "Malformed XML in batch file"),
    INGESTION_VALIDATION_FAILED("ING-002", "Transaction failed schema validation"),
    RECON_EXTERNAL_API_FAILURE("REC-001", "External enrichment API call failed"),
    RECON_UOW_COMMIT_FAILURE("REC-002", "Unit of work commit failed"),
    REVIEW_UNAUTHORIZED("REV-001", "Invalid or expired JWT"),
    REVIEW_NOT_FOUND("REV-002", "Review not found"),
    REVIEW_ALREADY_RESOLVED("REV-003", "Review has already been resolved");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
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