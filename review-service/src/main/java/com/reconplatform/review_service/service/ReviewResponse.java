package com.reconplatform.review_service.service;

import com.reconplatform.common.enums.MismatchReason;
import com.reconplatform.common.enums.ReviewStatus;
import com.reconplatform.review_service.model.FlaggedReviewEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewResponse(String reviewId, String transactionId, String accountNumber,
                             BigDecimal amount, String currency, BigDecimal convertedAmount,
                             MismatchReason mismatchReason, ReviewStatus status,
                             String reviewedBy, LocalDateTime reviewedAt, LocalDateTime createdAt) {

    public static ReviewResponse from(FlaggedReviewEntity e) {
        return new ReviewResponse(e.getReviewId(), e.getTransactionId(), e.getAccountNumber(),
                e.getAmount(), e.getCurrency(), e.getConvertedAmount(),
                e.getMismatchReason(), e.getStatus(),
                e.getReviewedBy(), e.getReviewedAt(), e.getCreatedAt());
    }
}