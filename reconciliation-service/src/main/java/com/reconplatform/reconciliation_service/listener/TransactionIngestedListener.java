package com.reconplatform.reconciliation_service.listener;

import com.reconplatform.common.messaging.KafkaTopics;
import com.reconplatform.common.dto.TransactionDTO;
import com.reconplatform.reconciliation_service.facade.ReconciliationFacade;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TransactionIngestedListener {

    private final ReconciliationFacade facade;

    public TransactionIngestedListener(ReconciliationFacade facade) {
        this.facade = facade;
    }

    @KafkaListener(topics = KafkaTopics.TRANSACTION_INGESTED)
    public void onMessage(TransactionDTO dto) {
        facade.process(dto);
    }
}