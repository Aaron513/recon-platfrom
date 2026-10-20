package com.reconplatform.review_service.service;

import com.reconplatform.common.dto.ReviewDecisionCommand;

public record ReviewResolvedEvent(ReviewDecisionCommand command) {}
