package com.reconplatform.review_service.service;

import com.reconplatform.common.dto.ReviewDecisionCommand;
import com.reconplatform.common.messaging.ActiveMqQueues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReviewDecisionSender {

    private static final Logger log = LoggerFactory.getLogger(ReviewDecisionSender.class);

    private final JmsTemplate jmsTemplate;

    public ReviewDecisionSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReviewResolved(ReviewResolvedEvent event) {
        ReviewDecisionCommand cmd = event.command();
        try {
            jmsTemplate.convertAndSend(ActiveMqQueues.MANUAL_REVIEW_RESOLVED, cmd.toMap());
            log.info("Sent {} decision for {}", cmd.decision(), cmd.transactionId());
        } catch (JmsException e) {
            log.error("Decision for {} saved but not sent to {}", cmd.transactionId(),
                    ActiveMqQueues.MANUAL_REVIEW_RESOLVED, e);
        }
    }
}