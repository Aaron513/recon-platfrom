package com.reconplatform.reconciliation_service.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reconplatform.common.dto.TransactionDTO;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class TransactionDtoJsonSerializer extends JsonSerializer<TransactionDTO> {

    public TransactionDtoJsonSerializer() {
        super(buildMapper());
    }

    private static ObjectMapper buildMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}