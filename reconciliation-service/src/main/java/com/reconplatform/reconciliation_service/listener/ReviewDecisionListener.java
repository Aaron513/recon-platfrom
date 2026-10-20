package com.reconplatform.reconciliation_service.listener;

import com.reconplatform.common.dto.ReviewDecisionCommand;
import com.reconplatform.common.messaging.ActiveMqQueues;
import com.reconplatform.reconciliation_service.service.ReviewDecisionService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReviewDecisionListener {

    private final ReviewDecisionService service;

    public ReviewDecisionListener(ReviewDecisionService service) {
        this.service = service;
    }

    @JmsListener(destination = ActiveMqQueues.MANUAL_REVIEW_RESOLVED)
    public void onMessage(Map<String, Object> message) {
        service.apply(ReviewDecisionCommand.fromMap(message));
    }
}