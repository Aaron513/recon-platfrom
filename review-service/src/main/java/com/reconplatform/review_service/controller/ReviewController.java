package com.reconplatform.review_service.controller;

import com.reconplatform.common.enums.ReviewStatus;
import com.reconplatform.review_service.service.ReviewResponse;
import com.reconplatform.review_service.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/pending")
    public List<ReviewResponse> pending() {
        return reviewService.findPending();
    }

    @PutMapping("/{reviewId}/approve")
    public ReviewResponse approve(@PathVariable String reviewId, Authentication authentication) {
        return reviewService.resolve(reviewId, ReviewStatus.APPROVED, authentication.getName());
    }

    @PutMapping("/{reviewId}/reject")
    public ReviewResponse reject(@PathVariable String reviewId, Authentication authentication) {
        return reviewService.resolve(reviewId, ReviewStatus.REJECTED, authentication.getName());
    }
}