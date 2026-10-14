package com.reconplatform.review_service.repository;

import com.reconplatform.review_service.model.FlaggedReviewEntity;
import com.reconplatform.common.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlaggedReviewRepository extends JpaRepository<FlaggedReviewEntity, String> {
    List<FlaggedReviewEntity> findByStatus(ReviewStatus status);
    boolean existsByTransactionId(String transactionId);
}