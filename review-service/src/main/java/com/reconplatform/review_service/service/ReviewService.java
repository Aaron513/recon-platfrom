package com.reconplatform.review_service.service;

import com.reconplatform.common.enums.ReviewStatus;
import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import com.reconplatform.review_service.model.FlaggedReviewEntity;
import com.reconplatform.review_service.repository.FlaggedReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final FlaggedReviewRepository repository;

    public ReviewService(FlaggedReviewRepository repository) {
        this.repository = repository;
    }

    public List<ReviewResponse> findPending() {
        return repository.findByStatus(ReviewStatus.PENDING).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse resolve(String reviewId, ReviewStatus decision, String reviewer) {
        FlaggedReviewEntity review = repository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getStatus() != ReviewStatus.PENDING) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_RESOLVED);
        }

        review.setStatus(decision);
        review.setReviewedBy(reviewer);
        review.setReviewedAt(LocalDateTime.now());
        FlaggedReviewEntity saved = repository.save(review);

        // ActiveMQ "manual-review-resolved" command goes here in the next step

        return ReviewResponse.from(saved);
    }
}