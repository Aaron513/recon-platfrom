package com.reconplatform.ingestion_service.rawbatchfile;

import com.reconplatform.common.dto.TransactionDTO;
import com.reconplatform.common.error.AppException;
import com.reconplatform.ingestion_service.service.BatchValidationService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Queue;

@Component
public class TransactionValidationProcessor implements Processor {

    private final TransactionMappingProcessor mapper;
    private final BatchValidationService validator;

    public TransactionValidationProcessor(TransactionMappingProcessor mapper, BatchValidationService validator) {
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public void process(Exchange exchange) {
        Queue<String> errors = exchange.getProperty("validationErrors", Queue.class);
        int record = exchange.getProperty(Exchange.SPLIT_INDEX, Integer.class) + 1;
        try {
            mapper.process(exchange);
            validator.validate(exchange.getIn().getBody(TransactionDTO.class));
        } catch (AppException e) {
            errors.add("record #" + record + " -> " + e.getErrorCode().getCode());
        } catch (Exception e) {
            errors.add("record #" + record + " -> unparseable: " + e.getMessage());
        }
    }
}