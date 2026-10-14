package com.reconplatform.review_service.listener;

import com.reconplatform.common.dto.TransactionDTO;
import com.reconplatform.common.enums.ReviewStatus;
import com.reconplatform.review_service.model.FlaggedReviewEntity;
import com.reconplatform.review_service.repository.FlaggedReviewRepository;
import com.reconplatform.common.messaging.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@Component
public class TransactionFlaggedListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionFlaggedListener.class);

    private final FlaggedReviewRepository repository;

    public TransactionFlaggedListener(FlaggedReviewRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = KafkaTopics.TRANSACTION_FLAGGED)
    public void onMessage(TransactionDTO dto) {
        if (dto.getFlagReason() == null) {
            log.error("Flagged message {} has no flagReason, skipping", dto.getTransactionId());
            return;
        }
        if (repository.existsByTransactionId(dto.getTransactionId())) {
            log.warn("Duplicate flag for {}, skipping", dto.getTransactionId());
            return;
        }
        FlaggedReviewEntity r = new FlaggedReviewEntity();
        r.setReviewId(UUID.randomUUID().toString());
        r.setTransactionId(dto.getTransactionId());
        r.setMismatchReason(dto.getFlagReason());
        r.setAccountNumber(dto.getAccountNumber());
        r.setAmount(dto.getAmount());
        r.setCurrency(dto.getCurrency());
        r.setConvertedAmount(dto.getConvertedAmount());
        r.setStatus(ReviewStatus.PENDING);
        repository.save(r);
    }
}